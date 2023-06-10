from unittest.mock import MagicMock

import pytest
from faker import Faker as BaseFaker

from core.jwt import JwtClient
from core.rabbitmq import RabbitMQClient
from core.redis import Cache

fake = BaseFaker()


@pytest.fixture
def mocked_rabbitmq_client():
    rabbitmq_mock = MagicMock(spec=RabbitMQClient)
    return rabbitmq_mock


@pytest.fixture
def mocked_cache():
    cache = MagicMock(spec=Cache)
    return cache


@pytest.fixture
def mocked_jwt_client():
    return MagicMock(spec=JwtClient)


@pytest.fixture
def user_data():
    return {
        "email": fake.email(),
        "first_name": fake.first_name(),
        "last_name": fake.last_name(),
        "password": fake.password(),
    }
