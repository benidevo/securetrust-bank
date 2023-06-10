import pytest
from django.urls import reverse
from rest_framework import status

from apps.user_auth.models import RefreshToken
from apps.user_auth.views.login import LoginView
from apps.user_auth.views.logout import LogoutView
from utils.factories import UserFactory

login_url = reverse("login")
logout_url = reverse("logout")
login_view = LoginView.as_view()
logout_view = LogoutView.as_view()


@pytest.mark.django_db
def test_logout_view(user_data, rf):
    password = user_data.get("password")
    user = UserFactory(
        email=user_data.get("email"),
        first_name=user_data.get("first_name"),
        last_name=user_data.get("last_name"),
    )
    user.set_password(password)
    user.is_active = True
    user.save()

    login_request = rf.post(login_url, data={"email": user.email, "password": password})
    login_response = login_view(login_request)

    access_token = login_response.data["data"]["access_token"]

    request = rf.delete(logout_url)
    request.META.update({"HTTP_AUTHORIZATION": f"Bearer {access_token}"})
    response = logout_view(request)

    user.refresh_from_db()
    assert response.status_code == status.HTTP_204_NO_CONTENT
    assert not RefreshToken.objects.filter(user=user).exists()
