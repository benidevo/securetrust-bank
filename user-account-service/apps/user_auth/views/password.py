from django.contrib.auth import get_user_model
from rest_framework import generics, status

from apps.user_auth.serializers import EmailSerializer, ResetPasswordSerializer
from core.rabbitmq import RabbitMQClient
from core.redis import Cache
from utils import Response, generate_otp
from utils.constants import RESET_PASSWORD_CACHE_KEY, RESET_PASSWORD_QUEUE

User = get_user_model()


class ForgotPasswordView(generics.GenericAPIView):
    serializer_class = EmailSerializer
    model = User
    rabbitmq_client = RabbitMQClient()

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        email = serializer.validated_data.get("email")

        user = self.model.objects.filter(email=email).first()
        if not user:
            return Response(
                success=True,
                message="If you have an account with this email, a verification code has been sent to your email",  # noqa
                status=status.HTTP_200_OK,
            )

        cache = self.get_cache()

        otp = generate_otp()
        message = {"first_name": user.first_name, "email": user.email, "otp": otp}

        cache.set(f"{RESET_PASSWORD_CACHE_KEY}{user.email}", otp, ttl=1200)
        with self.rabbitmq_client as client:
            client.publish_message(queue=RESET_PASSWORD_QUEUE, message=message)

        return Response(
            success=True,
            message="If you have an account with this email, a verification code has been sent to your email",  # noqa
            status=status.HTTP_200_OK,
        )

    def get_cache(self):
        return Cache()


class ResetPasswordView(generics.GenericAPIView):
    serializer_class = ResetPasswordSerializer
    model = User

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        data = serializer.validated_data
        email = data.get("email")

        cache = self.get_cache()
        cached_otp = cache.get(f"{RESET_PASSWORD_CACHE_KEY}{email}")
        if cached_otp != data.get("otp"):
            return Response(
                success=False,
                message="Invalid or expired OTP",
                status=status.HTTP_400_BAD_REQUEST,
            )

        user = self.model.objects.filter(email=email).first()
        if not user:
            return Response(
                success=False,
                message="Account not found",
                status=status.HTTP_400_BAD_REQUEST,
            )

        user.set_password(data.get("password"))
        user.save()
        cache.delete(f"{RESET_PASSWORD_CACHE_KEY}{email}")

        return Response(
            message="Password changed successfully",
            status=status.HTTP_200_OK,
        )

    def get_cache(self):
        return Cache()
