import logging

import jwt
from django.conf import settings
from django.contrib.auth import get_user_model
from rest_framework.authentication import BaseAuthentication
from rest_framework.exceptions import AuthenticationFailed

User = get_user_model()


class JWTAuthentication(BaseAuthentication):
    def authenticate(self, request):
        token = request.META.get("HTTP_AUTHORIZATION")

        if not token:
            return None

        if not token.startswith("Bearer "):
            raise AuthenticationFailed("Invalid token format")

        token = token.split(" ")[1]

        try:
            decoded_payload = jwt.decode(
                token,
                settings.JWT_AUTH["JWT_PUBLIC_KEY"],
                algorithms=[settings.JWT_AUTH["JWT_ALGORITHM"]],
            )
            user_id = decoded_payload.get("user_id")
            token_type = decoded_payload.get("token_type")

            if token_type != "access":
                raise AuthenticationFailed("Invalid token type")

            user = User.objects.filter(pk=user_id).first()
            if not user:
                raise AuthenticationFailed("Invalid or expired token")

            return (user, None)

        except (jwt.ExpiredSignatureError, jwt.DecodeError, jwt.InvalidTokenError) as e:
            message = f"Invalid token: {e}"
            logging.error(message)
            raise AuthenticationFailed("Invalid or expired token")
