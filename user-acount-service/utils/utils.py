import random

from rest_framework.response import Response as DRFResponse


def generate_otp() -> str:
    return str(random.randint(100000, 999999))


class Response(DRFResponse):
    def __init__(
        self,
        success=True,
        message=None,
        data=None,
        error=None,
        status=None,
        template_name=None,
        headers=None,
        exception=False,
        content_type=None,
    ):
        response_data = {
            "success": success,
            "message": message,
            "data": data,
            "error": error,
        }
        super().__init__(
            response_data,
            status=status,
            template_name=template_name,
            headers=headers,
            exception=exception,
            content_type=content_type,
        )
