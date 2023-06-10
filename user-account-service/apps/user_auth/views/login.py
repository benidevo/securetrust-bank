from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.exceptions import AuthenticationFailed

from apps.user_auth.models import RefreshToken
from apps.user_auth.serializers import LoginSerializer
from core.jwt import JwtClient
from utils import Response

User = get_user_model()


class LoginView(generics.GenericAPIView):
    serializer_class = LoginSerializer
    model = User
    jwt_client = JwtClient()

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        email = serializer.validated_data.get("email")
        password = serializer.validated_data.get("password")

        user = self.model.objects.filter(email=email).first()
        if not user or not user.check_password(password):
            raise AuthenticationFailed()

        access_token, refresh_token = self.jwt_client.generate_tokens(
            user_id=user.pk, user_role="user"
        )
        RefreshToken.objects.update_or_create(
            user=user, defaults={"token": refresh_token, "is_active": True}
        )

        data = {"access_token": access_token, "refresh_token": refresh_token}

        return Response(
            message="Login successful", data=data, status=status.HTTP_200_OK
        )
