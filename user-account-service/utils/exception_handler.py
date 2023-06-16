import logging

from rest_framework.exceptions import (
    AuthenticationFailed,
    NotAuthenticated,
    NotFound,
    PermissionDenied,
    ValidationError,
)
from rest_framework.views import exception_handler

from utils import Response
from utils.exceptions import AppException


def custom_exception_handler(exc, context):
    if isinstance(exc, ValidationError):
        return Response(
            success=False,
            message="Validation error.",
            error=exc.detail,
            status=exc.status_code,
        )

    if (
        isinstance(exc, NotFound)
        or isinstance(exc, PermissionDenied)
        or isinstance(exc, AuthenticationFailed)
        or isinstance(exc, NotAuthenticated)
        or isinstance(exc, AppException)
    ):
        logging.error(exc)
        return Response(success=False, message=exc.detail, status=exc.status_code)

    # return Response(success=False, message=exc.detail, status=exc.status_code)

    return exception_handler(exc, context)
