from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.exceptions import NotFound
from rest_framework.permissions import IsAuthenticated

from apps.users.serializers import UserSerializer
from utils import Response
from utils.permissions import IsAdmin

User = get_user_model()


class UserView(generics.GenericAPIView):
    model = User
    serializer_class = UserSerializer
    queryset = User.objects.all()
    permission_classes = [IsAuthenticated, IsAdmin]

    def get(self, request):
        users = self.get_queryset()

        serializer = self.get_serializer(users, many=True)
        return Response(
            message="Users returned successfully",
            data=serializer.data,
            status=status.HTTP_200_OK,
        )


class UserDetailView(generics.RetrieveUpdateDestroyAPIView):
    model = User
    serializer_class = UserSerializer
    permission_classes = [IsAuthenticated, IsAdmin]

    def get_object(self):
        user_id = self.kwargs.get("user_id")
        obj = self.model.objects.filter(id=user_id).first()
        if not obj:
            raise NotFound("User not found")
        return obj

    def get(self, request, *args, **kwargs):
        user = self.get_object()
        serializer = self.get_serializer(user)

        return Response(
            message="User returned", data=serializer.data, status=status.HTTP_200_OK
        )

    def update(self, request, *args, **kwargs):
        user = self.get_object()
        serializer = self.get_serializer(user, data=request.data)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(
            message="User updated", data=serializer.data, status=status.HTTP_200_OK
        )

    def partial_update(self, request, *args, **kwargs):
        user = self.get_object()
        serializer = self.get_serializer(user, data=request.data, partial=True)
        serializer.is_valid(raise_exception=True)
        serializer.save()

        return Response(
            message="User updated", data=serializer.data, status=status.HTTP_200_OK
        )
