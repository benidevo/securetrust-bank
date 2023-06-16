from rest_framework.exceptions import APIException


class AppException(APIException):
    def __init__(self, message, status):
        self.status_code = status
        self.detail = message
