from unittest.mock import ANY, patch

import pytest
from django.contrib.auth import get_user_model
from django.urls import reverse
from faker import Faker
from rest_framework import status

from apps.user_auth.views.password import ForgotPasswordView, ResetPasswordView
from utils import generate_otp
from utils.constants import RESET_PASSWORD_CACHE_KEY, RESET_PASSWORD_QUEUE
from utils.factories import UserFactory

forgot_password_url = reverse("forgot-password")
reset_password_url = reverse("reset-password")
forgot_password_view = ForgotPasswordView.as_view()
reset_password_view = ResetPasswordView.as_view()
User = get_user_model()

fake = Faker()


@pytest.mark.django_db
class TestForgotPasswordView:
    def test_account_not_found(self, user_data, rf):
        email = user_data.get("email")

        request = rf.post(forgot_password_url, data={"email": email})
        response = forgot_password_view(request)

        assert response.status_code == status.HTTP_200_OK
        assert response.data["success"]
        assert (
            response.data["message"]
            == "If you have an account with this email, a verification code has been sent to your email"  # noqa
        )

    def test_forgot_password_success(
        self, user_data, mocked_rabbitmq_client, mocked_cache, rf
    ):
        password = user_data.get("password")
        user = UserFactory(
            email=user_data.get("email"),
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
        )
        user.set_password(password)
        user.save()

        request = rf.post(forgot_password_url, data={"email": user.email})

        mocked_rabbitmq_client.__enter__.return_value = mocked_rabbitmq_client
        with patch.object(
            ForgotPasswordView, "rabbitmq_client", mocked_rabbitmq_client
        ):
            with patch.object(ForgotPasswordView, "cache", mocked_cache):
                response = forgot_password_view(request)

        assert response.status_code == status.HTTP_200_OK
        assert response.data["success"]
        assert (
            response.data["message"]
            == "If you have an account with this email, a verification code has been sent to your email"  # noqa
        )
        mocked_rabbitmq_client.publish_message.assert_called_once_with(
            queue=RESET_PASSWORD_QUEUE, message=ANY
        )
        mocked_cache.set.assert_called_once_with(
            key=f"{RESET_PASSWORD_CACHE_KEY}{user.email}", value=ANY, ttl=1200
        )


@pytest.mark.django_db
class TestResetPasswordView:
    def test_invalid_or_expired_otp(self, mocked_cache, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            email=user_data.get("email"),
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
        )
        user.set_password(password)
        user.save()
        new_password = fake.password()

        valid_otp = generate_otp()
        invalid_otp = generate_otp()
        mocked_cache.get.return_value = valid_otp

        request = rf.post(
            reset_password_url,
            data={"email": user.email, "otp": invalid_otp, "password": new_password},
        )

        with patch.object(ResetPasswordView, "cache", mocked_cache):
            response = reset_password_view(request)

        assert response.status_code == 400
        assert not response.data["success"]
        assert response.data["message"] == "Invalid or expired OTP"
        mocked_cache.get.assert_called_once_with(
            key=f"{RESET_PASSWORD_CACHE_KEY}{user.email}"
        )
        assert mocked_cache.get.return_value != invalid_otp

    def test_account_not_found(self, mocked_cache, user_data, rf):
        password = user_data.get("password")
        email = user_data.get("email")

        otp = generate_otp()
        mocked_cache.get.return_value = otp

        request = rf.post(
            reset_password_url,
            data={"email": email, "otp": otp, "password": password},
        )

        with patch.object(ResetPasswordView, "cache", mocked_cache):
            response = reset_password_view(request)

        assert response.status_code == status.HTTP_400_BAD_REQUEST
        assert not response.data["success"]
        assert response.data["message"] == "Account not found"
        mocked_cache.get.assert_called_once_with(
            key=f"{RESET_PASSWORD_CACHE_KEY}{email}"
        )
        assert mocked_cache.get.return_value == otp

    def test_reset_password_success(self, mocked_cache, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            email=user_data.get("email"),
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
        )
        user.set_password(password)
        user.save()

        new_password = fake.password()
        otp = generate_otp()
        mocked_cache.get.return_value = otp

        request = rf.post(
            reset_password_url,
            data={"email": user.email, "password": new_password, "otp": otp},
        )

        with patch.object(ResetPasswordView, "cache", mocked_cache):
            response = reset_password_view(request)

        user.refresh_from_db()
        assert response.status_code == status.HTTP_200_OK
        assert response.data["success"]
        assert response.data["message"] == "Password changed successfully"
        mocked_cache.get.called_once_with(key=f"{RESET_PASSWORD_CACHE_KEY}{user.email}")
        mocked_cache.delete.called_once_with(
            key=f"{RESET_PASSWORD_CACHE_KEY}{user.email}"
        )
        assert mocked_cache.get.return_value == otp
        assert user.check_password(new_password)
