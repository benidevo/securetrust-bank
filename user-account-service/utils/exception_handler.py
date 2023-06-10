import logging

from rest_framework.exceptions import (
    AuthenticationFailed,
    NotFound,
    PermissionDenied,
    ValidationError,
)
from rest_framework.views import exception_handler

from utils import Response


def custom_exception_handler(exc, context):
    if isinstance(exc, ValidationError):
        return Response(
            success=False,
            message="Validation Error",
            error=exc.detail,
            status=exc.status_code,
        )

    if (
        isinstance(exc, NotFound)
        or isinstance(exc, PermissionDenied)
        or isinstance(exc, AuthenticationFailed)
    ):
        logging.error(exc)
        return Response(success=False, message=exc.detail, status=exc.status_code)

    # return Response(success=False, message=exc.detail, status=exc.status_code)

    return exception_handler(exc, context)
