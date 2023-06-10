from django.contrib.auth import get_user_model
from rest_framework import generics, status

from apps.user_auth.serializers import (
    EmailSerializer,
    UserRegistrationSerializer,
    VerifyEmailSerializer,
)
from core.rabbitmq import RabbitMQClient
from core.redis import Cache
from utils import Response, generate_otp
from utils.constants import EMAIL_VERIFICATION_QUEUE, VERIFY_EMAIL_CACHE_KEY

User = get_user_model()


class RegisterView(generics.CreateAPIView):
    serializer_class = UserRegistrationSerializer
    model = User
    rabbitmq_client = RabbitMQClient()
    cache = Cache()

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        self.perform_create(serializer)

        otp = generate_otp()
        data = serializer.validated_data
        email = data.get("email")

        self.cache.set(key=f"{VERIFY_EMAIL_CACHE_KEY}{email}", value=otp, ttl=1200)

        message = {"first_name": data.get("first_name"), "otp": otp, "email": email}
        with self.rabbitmq_client as client:
            client.publish_message(queue=EMAIL_VERIFICATION_QUEUE, message=message)

        return Response(
            success=True,
            message="Registration successful. A verification code has been sent to your email",  # noqa: E501
            status=status.HTTP_201_CREATED,
        )


class ResendEmailVerificationView(generics.GenericAPIView):
    serializer_class = EmailSerializer
    model = User
    cache = Cache()
    rabbitmq_client = RabbitMQClient()

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        email = serializer.validated_data.get("email")
        user = self.model.objects.filter(email=email).first()
        if not user:
            return Response(
                success=False,
                message="Account does not exist",
                status=status.HTTP_400_BAD_REQUEST,
            )

        if user.is_active:
            return Response(
                success=False,
                message="Email already verified",
                status=status.HTTP_400_BAD_REQUEST,
            )

        otp = generate_otp()

        self.cache.set(key=f"{VERIFY_EMAIL_CACHE_KEY}{email}", value=otp, ttl=1200)

        message = {"first_name": user.first_name, "otp": otp, "email": email}
        with self.rabbitmq_client as client:
            client.publish_message(queue=EMAIL_VERIFICATION_QUEUE, message=message)

        return Response(
            success=True,
            message="A verification code has been sent to your email",
            status=status.HTTP_200_OK,
        )


class VerifyEmailView(generics.GenericAPIView):
    serializer_class = VerifyEmailSerializer
    model = User
    cache = Cache()

    def post(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        data = serializer.validated_data
        email = data.get("email")
        otp = data.get("otp")

        cached_otp = self.cache.get(f"{VERIFY_EMAIL_CACHE_KEY}{email}")
        if not cached_otp:
            return Response(
                success=False,
                message="Invalid or expired otp",
                status=status.HTTP_400_BAD_REQUEST,
            )

        if cached_otp != otp:
            return Response(
                success=False,
                message="Invalid or expired otp",
                status=status.HTTP_400_BAD_REQUEST,
            )

        user = self.model.objects.filter(email=email).first()
        user.is_active = True
        user.save()
        self.cache.delete(f"{VERIFY_EMAIL_CACHE_KEY}{email}")

        return Response(
            success=True,
            message="Email Verified",
            status=status.HTTP_200_OK,
        )

    def get_cache(self):
        return Cache()
