import pytest
from django.urls import reverse
from rest_framework import status

from apps.user_auth.views.auth_user import AuthUserView
from apps.user_auth.views.login import LoginView
from apps.users.serializers import UserSerializer
from utils.factories import UserFactory

auth_user_url = reverse("auth-user")
login_url = reverse("login")

view = AuthUserView.as_view()
login_view = LoginView.as_view()


@pytest.mark.django_db
def test_get_user__unauthenticated(user_data, rf):
    password = user_data.get("password")
    user = UserFactory(
        email=user_data.get("email"),
        first_name=user_data.get("first_name"),
        last_name=user_data.get("last_name"),
    )
    user.is_active = True
    user.set_password(password)
    user.save()

    request = rf.get(auth_user_url)
    response = view(request)

    assert response.status_code == status.HTTP_403_FORBIDDEN
    assert not response.data["success"]
    assert response.data["message"] == "Authentication credentials were not provided."


@pytest.mark.django_db
def test_get_user__success(user_data, rf):
    password = user_data.get("password")
    user = UserFactory(
        email=user_data.get("email"),
        first_name=user_data.get("first_name"),
        last_name=user_data.get("last_name"),
    )
    user.is_active = True
    user.set_password(password)
    user.save()

    login_request = rf.post(login_url, data={"email": user.email, "password": password})
    login_response = login_view(login_request)

    access_token = login_response.data["data"]["access_token"]

    request = rf.get(auth_user_url)
    request.META.update({"HTTP_AUTHORIZATION": f"Bearer {access_token}"})
    response = view(request)

    serializer = UserSerializer(user)

    assert response.status_code == status.HTTP_200_OK
    assert response.data["success"]
    assert response.data["message"] == "User retrieved"
    assert response.data["data"] == serializer.data
