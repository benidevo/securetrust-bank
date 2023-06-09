import pytest
from django.contrib.auth import get_user_model
from django.test import override_settings
from faker import Faker as BaseFaker

from apps.users.models import Profile

from .factories import UserFactory

User = get_user_model()
fake = BaseFaker()


@pytest.mark.django_db
def test_create_user(user_data):
    user = User.objects.create_user(**user_data)
    assert user.email == user_data["email"]
    assert user.first_name == user_data["first_name"]
    assert user.last_name == user_data["last_name"]
    assert user.is_active == False  # noqa: E712
    assert user.is_staff == False  # noqa: E712
    assert user.created_at is not None
    assert user.updated_at is not None


@pytest.mark.django_db
def test_create_profile(user_data):
    user = UserFactory.create(**user_data)
    profile = Profile.objects.filter(user=user).first()

    assert profile.user == user


@pytest.mark.django_db
def test_profile_is_complete(user_data):
    user = UserFactory.create(**user_data)
    profile = user.profile
    complete_profile = {
        "avatar_url": fake.url(),
        "gender": fake.random_element(elements=["M", "F", "O"]),
        "phone_number": fake.phone_number(),
        "address": fake.address(),
        "city": fake.city(),
        "country": fake.country_code(),
        "date_of_birth": fake.date_of_birth().strftime("%Y-%m-%d"),
        "nin": fake.random_int(min=10000000000, max=99999999999),
    }
    for field, value in complete_profile.items():
        setattr(profile, field, value)
    profile.save()

    assert profile.is_complete == True  # noqa: E712


@pytest.mark.django_db
def test_profile_is_not_complete(user_data):
    user = UserFactory.create(**user_data)
    profile = user.profile
    profile.phone_number = None
    profile.save()
    assert profile.is_complete == False  # noqa: E712


@override_settings(USE_TZ=True)
@pytest.mark.django_db
def test_user_profile_created_signal(user_data):
    user = UserFactory.create(**user_data)
    assert Profile.objects.filter(user=user).exists()
