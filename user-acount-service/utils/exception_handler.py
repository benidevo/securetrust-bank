from rest_framework.exceptions import ValidationError
from rest_framework.response import Response
from rest_framework.views import exception_handler


def custom_exception_handler(exc, context):
    if isinstance(exc, ValidationError):
        response_data = {
            "success": False,
            "message": "Validation Error",
            "data": None,
            "error": exc.detail,
        }
        return Response(response_data, status=exc.status_code)

    return exception_handler(exc, context)
