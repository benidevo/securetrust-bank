import pytest
from faker import Faker as BaseFaker

fake = BaseFaker()


@pytest.fixture
def user_data():
    return {
        "email": fake.email(),
        "first_name": fake.first_name(),
        "last_name": fake.last_name(),
        "password": fake.password(),
    }
