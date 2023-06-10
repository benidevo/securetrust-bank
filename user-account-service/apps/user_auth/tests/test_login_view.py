from unittest.mock import ANY, patch

import pytest
from django.contrib.auth import get_user_model
from django.urls import reverse
from rest_framework import status
from rest_framework.exceptions import AuthenticationFailed

from apps.user_auth.views.login import LoginView
from utils.factories import UserFactory

url = reverse("login")
view = LoginView.as_view()
User = get_user_model()


@pytest.mark.django_db
def test_login_incorrect_credentials(rf):
    user = UserFactory()

    wrong_password = "wrongPassword2"
    request = rf.post(url, data={"email": user.email, "password": wrong_password})

    response = view(request)

    assert response.status_code == status.HTTP_403_FORBIDDEN
    assert not response.data["success"]
    assert response.data["message"] == AuthenticationFailed.default_detail


@pytest.mark.django_db
def test_login_validation_failure(rf):
    user = UserFactory()
    invalid_email = "invalid.com"

    request = rf.post(url, data={"email": invalid_email, "password": user.password})
    response = view(request)

    assert response.status_code == status.HTTP_400_BAD_REQUEST
    assert not response.data["success"]
    assert response.data["message"] == "Validation error."
    assert response.data["error"]


@pytest.mark.django_db
def test_login_inactive_user(user_data, rf):
    password = user_data.get("password")
    user = UserFactory(
        email=user_data["email"],
        first_name=user_data["first_name"],
        last_name=user_data["last_name"],
    )
    user.set_password(password)
    user.save()

    request = rf.post(url, data={"email": user.email, "password": password})
    response = view(request)

    assert response.status_code == status.HTTP_400_BAD_REQUEST
    assert not response.data["success"]
    assert response.data["message"] == "Inactive account"


@pytest.mark.django_db
def test_login_success(user_data, mocked_jwt_client, rf):
    password = user_data.get("password")
    user = UserFactory(
        email=user_data["email"],
        first_name=user_data["first_name"],
        last_name=user_data["last_name"],
    )
    user.set_password(password)
    user.is_active = True
    user.save()

    mocked_jwt_client.generate_tokens.return_value = (ANY, ANY)

    request = rf.post(url, data={"email": user.email, "password": password})
    with patch.object(LoginView, "jwt_client", mocked_jwt_client):
        response = view(request)

    assert response.status_code == status.HTTP_200_OK
    assert response.data["message"] == "Login successful"
    assert response.data["success"]
    data = response.data["data"]
    assert isinstance(data, dict)
    assert "access_token" in data and data["access_token"] == ANY
    assert "refresh_token" in data and data["access_token"] == ANY
    isinstance(data["access_token"], str)
    isinstance(data["refresh_token"], str)
    mocked_jwt_client.generate_tokens.assert_called_once_with(
        user_id=user.pk, user_role=user.role
    )
    assert mocked_jwt_client.generate_tokens.return_value == (ANY, ANY)
