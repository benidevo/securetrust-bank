from .base import *  # noqa
from .base import env

# SECURITY WARNING: keep the secret key used in production secret!
SECRET_KEY = env(
    "DJANGO_SECRET_KEY", default="b#-1)@q6z9a=(f6!drg##qj*60r(amfd)d!6_!_*a^a@m2n(a1"
)

# SECURITY WARNING: don't run with debug turned on in production!
DEBUG = True

CSRF_TRUSTED_ORIGINS = ["http://localhost:8080"]

DOMAIN = env("DOMAIN", default="localhost")
SITE_NAME = "securetrustbank"
