from rest_framework.permissions import BasePermission

from .constants import ADMIN_USER


class IsAdmin(BasePermission):
    def has_permission(self, request, view):
        return request.user.role == ADMIN_USER
