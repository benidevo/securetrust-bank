import json

from django.contrib.auth import get_user_model
from rest_framework import generics, status

from core.rabbitmq import RabbitMQClient
from core.redis import Cache
from utils import Response, generate_otp
from utils.constants import EMAIL_VERIFICATION_QUEUE, VERIFY_EMAIL_CACHE_KEY

from .serializers import UserRegistrationSerializer


class RegisterView(generics.CreateAPIView):
    serializer_class = UserRegistrationSerializer
    model = get_user_model()
    cache = Cache
    rabbitmq_client = RabbitMQClient()

    def post(self, request, *args, **kwargs):
        with self.rabbitmq_client:
            response = super().post(request, *args, **kwargs)
        return response

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        self.perform_create(serializer)

        otp = generate_otp()
        data = serializer.validated_data
        email = data.get("email")
        cache = self.get_cache()
        cache.set(f"{VERIFY_EMAIL_CACHE_KEY}{email}", otp, ttl=1200)

        message = {"first_name": data.get("first_name"), "otp": otp, "email": email}
        self.rabbitmq_client.publish_message(
            queue=EMAIL_VERIFICATION_QUEUE, message=json.dumps(message)
        )

        return Response(
            success=True,
            message="Registration successful. A verification code has been sent to your email",  # noqa: E501
            status=status.HTTP_200_OK,
        )

    def get_cache(self):
        return self.cache()
