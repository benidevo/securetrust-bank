import os

from .base import *  # noqa

SECRET_KEY = os.environ.get(
    "DJANGO_SECRET_KEY", "b#-1)@q6z9a=(f6!drg##qj*60r(amfd)d!6_!_*a^a@m2n(a1"
)

DEBUG = True

CSRF_TRUSTED_ORIGINS = ["http://localhost:8080"]

DOMAIN = os.environ.get("DOMAIN", "localhost")
SITE_NAME = "securetrustbank"
