from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.exceptions import AuthenticationFailed, NotFound
from rest_framework.permissions import IsAuthenticated

from apps.user_auth.models import RefreshToken
from apps.user_auth.serializers import EmailSerializer, RefreshTokenSerializer
from core.jwt import JwtClient
from utils import Response
from utils.permissions import IsAdmin

User = get_user_model()


class RefreshTokenView(generics.GenericAPIView):
    serializer_class = RefreshTokenSerializer
    model = User
    jwt_client = JwtClient()

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        refresh_token = serializer.validated_data.get("refresh_token")

        user = self.jwt_client.verify_refresh_token(refresh_token)

        token = RefreshToken.objects.filter(user=user).first()
        if token and not token.is_active:
            raise AuthenticationFailed("Invalid or expired token")

        access_token, _ = self.jwt_client.generate_tokens(
            user_id=user.pk, user_role="user"
        )

        token.is_active = False
        token.save()

        data = {
            "access_token": access_token,
        }
        return Response(data=data, status=status.HTTP_200_OK)


class DeleteRefreshToken(generics.DestroyAPIView):
    serializer_class = EmailSerializer
    model = RefreshToken
    permission_classes = [IsAuthenticated, IsAdmin]

    def destroy(self, request, *args, **kwargs):
        obj = self.get_object()
        if obj:
            self.perform_destroy(obj)

        return Response(status=status.HTTP_204_NO_CONTENT)

    def get_object(self):
        serializer = self.get_serializer(data=self.request.data)
        serializer.is_valid(raise_exception=True)
        email = serializer.validated_data.get("email")

        try:
            user = User.objects.get(email=email)
        except User.DoesNotExist:
            raise NotFound("Account does not exist")

        return (
            user.refresh_token
            if not User.refresh_token.RelatedObjectDoesNotExist
            else None
        )
