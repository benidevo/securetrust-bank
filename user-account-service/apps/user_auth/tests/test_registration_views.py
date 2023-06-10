from unittest.mock import ANY, patch

import pytest
from django.urls import reverse
from faker import Faker
from rest_framework import status

from apps.user_auth.views.registration import (
    RegisterView,
    ResendEmailVerificationView,
    VerifyEmailView,
)
from utils import generate_otp
from utils.constants import EMAIL_VERIFICATION_QUEUE, VERIFY_EMAIL_CACHE_KEY
from utils.factories import UserFactory

fake = Faker()


@pytest.mark.django_db
class TestRegisterView:
    def test_post(self, mocked_rabbitmq_client, mocked_cache, user_data, rf):
        url = reverse("user-register")

        request = rf.post(
            url,
            data=user_data,
        )
        view = RegisterView.as_view()

        mocked_rabbitmq_client.__enter__.return_value = mocked_rabbitmq_client

        with patch.object(RegisterView, "rabbitmq_client", mocked_rabbitmq_client):
            with patch.object(RegisterView, "cache", mocked_cache):
                response = view(request)

        assert response.status_code == status.HTTP_201_CREATED
        assert response.data["success"] is True
        assert (
            response.data["message"]
            == "Registration successful. A verification code has been sent to your email"  # noqa
        )
        mocked_cache.set.assert_called_once_with(
            key=f"{VERIFY_EMAIL_CACHE_KEY}{user_data.get('email')}",
            value=ANY,
            ttl=ANY,
        )
        mocked_rabbitmq_client.publish_message.assert_called_once_with(
            queue=EMAIL_VERIFICATION_QUEUE, message=ANY
        )


@pytest.mark.django_db
class TestResendEmailVerificationView:
    def test_post_existing_user_not_verified(
        self, mocked_rabbitmq_client, mocked_cache, user_data, rf
    ):
        user = UserFactory(
            email=user_data.get("email"), password=user_data.get("password")
        )

        url = reverse("resend-verification")
        request = rf.post(url, data={"email": user.email})
        view = ResendEmailVerificationView.as_view()

        mocked_rabbitmq_client.__enter__.return_value = mocked_rabbitmq_client

        with patch.object(
            ResendEmailVerificationView, "rabbitmq_client", mocked_rabbitmq_client
        ):
            with patch.object(ResendEmailVerificationView, "cache", mocked_cache):
                response = view(request)

        assert response.status_code == status.HTTP_200_OK
        assert response.data["success"] is True
        assert (
            response.data["message"]
            == "A verification code has been sent to your email"
        )
        mocked_rabbitmq_client.publish_message.assert_called_once_with(
            queue=EMAIL_VERIFICATION_QUEUE, message=ANY
        )
        mocked_cache.set.assert_called_once_with(
            key=f"{VERIFY_EMAIL_CACHE_KEY}{user.email}", value=ANY, ttl=ANY
        )

    def test_post_existing_user_already_verified(
        self, mocked_rabbitmq_client, mocked_cache, user_data, rf
    ):
        user = UserFactory(
            email=user_data.get("email"),
            password=user_data.get("password"),
            is_active=True,
        )

        url = reverse("resend-verification")
        request = rf.post(url, data={"email": user.email})
        view = ResendEmailVerificationView.as_view()

        response = view(request)

        assert response.status_code == status.HTTP_400_BAD_REQUEST
        assert response.data["success"] is False
        assert response.data["message"] == "Email already verified"
        mocked_cache.set.assert_not_called()
        mocked_rabbitmq_client.publish_message.assert_not_called()

    def test_post_nonexistent_user(self, mocked_rabbitmq_client, mocked_cache, rf):
        url = reverse("resend-verification")
        request = rf.post(url, data={"email": "nonexistent@example.com"})
        view = ResendEmailVerificationView.as_view()

        response = view(request)

        assert response.status_code == status.HTTP_400_BAD_REQUEST
        assert response.data["success"] is False
        assert response.data["message"] == "Account does not exist"
        mocked_cache.set.assert_not_called()
        mocked_rabbitmq_client.publish_message.assert_not_called()


@pytest.mark.django_db
class TestVerifyEmailView:
    def test_post_valid_otp(self, mocked_cache, rf):
        user = UserFactory(email="test@example.com", password="Test4321")

        otp = generate_otp()
        mocked_cache.get.return_value = otp

        url = reverse("verify-email")
        request = rf.post(url, data={"email": user.email, "otp": otp})
        view = VerifyEmailView.as_view()

        with patch.object(VerifyEmailView, "cache", mocked_cache):
            response = view(request)

        assert response.status_code == status.HTTP_200_OK
        assert response.data["success"] is True
        assert response.data["message"] == "Email Verified"

        mocked_cache.get.assert_called_once_with(
            f"{VERIFY_EMAIL_CACHE_KEY}{user.email}"
        )
        mocked_cache.delete.assert_called_once_with(
            f"{VERIFY_EMAIL_CACHE_KEY}{user.email}"
        )

    def test_post_invalid_otp(self, mocked_cache, rf):
        user = UserFactory(email="test@example.com", password="Test4321")

        cached_otp = generate_otp()
        wrong_otp = generate_otp()

        mocked_cache.get.return_value = cached_otp

        url = reverse("verify-email")
        request = rf.post(url, data={"email": user.email, "otp": wrong_otp})
        view = VerifyEmailView.as_view()

        with patch.object(VerifyEmailView, "cache", mocked_cache):
            response = view(request)

        assert response.status_code == status.HTTP_400_BAD_REQUEST
        assert response.data["success"] is False
        assert response.data["message"] == "Invalid or expired otp"

        mocked_cache.get.assert_called_once_with(
            f"{VERIFY_EMAIL_CACHE_KEY}{user.email}"
        )
        mocked_cache.delete.assert_not_called()
