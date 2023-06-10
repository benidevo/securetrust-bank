import logging

import jwt
from django.conf import settings
from django.contrib.auth import get_user_model
from django.utils import timezone
from rest_framework.exceptions import AuthenticationFailed

User = get_user_model()


class JwtClient:
    @staticmethod
    def generate_tokens(user_id, user_role):
        access_payload = {
            "user_id": user_id,
            "user_role": user_role,
            "token_type": "access",
            "exp": timezone.now() + settings.JWT_AUTH["JWT_ACCESS_TOKEN_EXPIRATION"],
        }
        access_token = jwt.encode(
            access_payload,
            settings.JWT_AUTH["JWT_PRIVATE_KEY"],
            algorithm=settings.JWT_AUTH["JWT_ALGORITHM"],
        )

        refresh_payload = {
            "user_id": user_id,
            "token_type": "refresh",
            "exp": timezone.now() + settings.JWT_AUTH["JWT_REFRESH_TOKEN_EXPIRATION"],
        }
        refresh_token = jwt.encode(
            refresh_payload,
            settings.JWT_AUTH["JWT_PRIVATE_KEY"],
            algorithm=settings.JWT_AUTH["JWT_ALGORITHM"],
        )

        return access_token, refresh_token

    @staticmethod
    def verify_refresh_token(refresh_token):
        try:
            decoded_payload = jwt.decode(
                refresh_token,
                settings.JWT_AUTH["JWT_PUBLIC_KEY"],
                algorithms=[settings.JWT_AUTH["JWT_ALGORITHM"]],
            )
            user_id = decoded_payload.get("user_id")
            token_type = decoded_payload.get("token_type")

            if token_type != "refresh":
                raise AuthenticationFailed("Invalid token type")

            user = User.objects.filter(pk=user_id).first()
            if not user:
                raise AuthenticationFailed("Invalid or expired token")

            return user

        except (jwt.ExpiredSignatureError, jwt.DecodeError, jwt.InvalidTokenError) as e:
            message = f"Invalid token: {e}"
            logging.error(message)
            raise AuthenticationFailed("Invalid or expired token")
