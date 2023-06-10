import jwt
import pytest
from django.urls import reverse
from faker import Faker
from rest_framework import status

from apps.user_auth.models import RefreshToken
from apps.user_auth.views.login import LoginView
from apps.user_auth.views.token import DeleteRefreshTokenView, RefreshTokenView
from utils.constants import ADMIN_USER
from utils.factories import UserFactory

faker = Faker()
login_url = reverse("login")
refresh_token_url = reverse("refresh-token")
delete_refresh_token_url = reverse("delete-refresh-token")
login_view = LoginView.as_view()
refresh_token_view = RefreshTokenView.as_view()
delete_refresh_token_view = DeleteRefreshTokenView.as_view()


@pytest.mark.django_db
class TestRefreshTokenView:
    def test_invalid_or_expired_token(self, rf):
        secret_key = faker.pystr(min_chars=32, max_chars=64)
        payload = {
            "user_id": faker.random_number(digits=6),
            "role": "USER",
        }

        token = jwt.encode(payload, secret_key, algorithm="HS256")
        refresh_token = token

        request = rf.post(refresh_token_url, data={"refresh_token": refresh_token})
        response = refresh_token_view(request)

        assert response.status_code == status.HTTP_403_FORBIDDEN
        assert not response.data["success"]
        assert response.data["message"] == "Invalid or expired token"

    def test_refresh_token_success(self, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            email=user_data.get("email"),
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
        )
        user.set_password(password)
        user.is_active = True
        user.save()

        login_request = rf.post(
            login_url, data={"email": user.email, "password": password}
        )
        login_response = login_view(login_request)

        refresh_token = login_response.data["data"]["refresh_token"]

        request = rf.post(refresh_token_url, data={"refresh_token": refresh_token})
        response = refresh_token_view(request)

        assert response.status_code == status.HTTP_200_OK
        data = response.data["data"]
        assert isinstance(data, dict)
        assert data.get("access_token")
        assert not RefreshToken.objects.filter(user=user, is_active=True).exists()

    def test_invalid_token_type(self, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            email=user_data.get("email"),
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
        )
        user.set_password(password)
        user.is_active = True
        user.save()

        login_request = rf.post(
            login_url, data={"email": user.email, "password": password}
        )
        login_response = login_view(login_request)

        access_token = login_response.data["data"]["access_token"]

        request = rf.post(refresh_token_url, data={"refresh_token": access_token})
        response = refresh_token_view(request)

        assert response.status_code == status.HTTP_403_FORBIDDEN
        assert not response.data["success"]
        assert response.data["message"] == "Invalid token type"


@pytest.mark.django_db
class TestDeleteRefreshToken:
    def test_no_permission(self, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
            email=user_data.get("email"),
        )
        user.set_password(password)
        user.is_active = True
        user.save()

        login_request = rf.post(
            login_url, data={"email": user.email, "password": password}
        )
        login_response = login_view(login_request)

        access_token = login_response.data["data"]["access_token"]

        request = rf.delete(delete_refresh_token_url, data={"email": user.email})
        request.META.update({"HTTP_AUTHORIZATION": f"Bearer {access_token}"})
        response = delete_refresh_token_view(request)

        assert response.status_code == status.HTTP_403_FORBIDDEN
        assert not response.data["success"]
        assert (
            response.data["message"]
            == "You do not have permission to perform this action."
        )

    def test_account_does_not_exist(self, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
            email=user_data.get("email"),
        )
        user.set_password(password)
        user.is_active = True
        user.role = ADMIN_USER
        user.save()

        login_request = rf.post(
            login_url, data={"email": user.email, "password": password}
        )
        login_response = login_view(login_request)

        access_token = login_response.data["data"]["access_token"]

        request = rf.delete(
            delete_refresh_token_url,
            content_type="application/json",
            data={"email": "nonexistinguser@gmail.com"},
        )
        request.META.update({"HTTP_AUTHORIZATION": f"Bearer {access_token}"})
        response = delete_refresh_token_view(request)

        assert response.status_code == status.HTTP_404_NOT_FOUND
        assert not response.data["success"]
        assert response.data["message"] == "Account does not exist"

    def test_delete_refresh_token_success(self, user_data, rf):
        password = user_data.get("password")
        user = UserFactory(
            first_name=user_data.get("first_name"),
            last_name=user_data.get("last_name"),
            email=user_data.get("email"),
        )
        user.set_password(password)
        user.is_active = True
        user.role = ADMIN_USER
        user.save()

        login_request = rf.post(
            login_url, data={"email": user.email, "password": password}
        )
        login_response = login_view(login_request)

        access_token = login_response.data["data"]["access_token"]

        request = rf.delete(
            delete_refresh_token_url,
            content_type="application/json",
            data={"email": user.email},
        )
        request.META.update({"HTTP_AUTHORIZATION": f"Bearer {access_token}"})
        response = delete_refresh_token_view(request)

        assert response.status_code == status.HTTP_204_NO_CONTENT
