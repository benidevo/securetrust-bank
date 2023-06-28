from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.exceptions import PermissionDenied
from rest_framework.permissions import IsAuthenticated

from apps.users.serializers import UserSerializer
from core.rabbitmq import RabbitMQClient
from utils import Response
from utils.constants import CREATE_BANK_ACCOUNT_QUEUE
from utils.exceptions import AppException

User = get_user_model()


class AuthUserView(generics.RetrieveUpdateAPIView):
    model = User
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated]
    rabbitmq_client = RabbitMQClient()

    def get_object(self):
        obj = self.model.objects.filter(pk=self.request.user.id).first()
        if not obj:
            raise PermissionDenied()
        return obj

    def retrieve(self, request, *args, **kwargs):
        user = self.get_object()
        serializer = self.get_serializer(user)

        return Response(
            message="User retrieved", data=serializer.data, status=status.HTTP_200_OK
        )

    def update(self, request, *args, **kwargs):
        user = self.get_object()
        if user.profile.is_complete:
            raise AppException(
                message="Contact support to update your profile",
                status=status.HTTP_403_FORBIDDEN,
            )

        request.data.pop("is_active", False)

        profile_data = request.data.pop("profile", None)
        if profile_data:
            profile_serializer = self.serializer_class().fields["profile"]
            profile_instance = user.profile
            profile = profile_serializer.update(profile_instance, profile_data)
            user.profile = profile

        serializer = self.get_serializer(user, data=request.data, partial=True)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        if user.profile.is_complete:
            message = {
                "userId": user.id,
                "name": f"{user.first_name} {user.last_name}",
                "email": user.email,
            }
            with self.rabbitmq_client as client:
                client.publish_message(queue=CREATE_BANK_ACCOUNT_QUEUE, message=message)

        return Response(
            message="User updated", data=serializer.data, status=status.HTTP_200_OK
        )
