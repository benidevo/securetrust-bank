from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated

from apps.user_auth.models import RefreshToken
from utils import Response

User = get_user_model()


class LogoutView(generics.GenericAPIView):
    model = User
    permission_classes = [IsAuthenticated]

    def delete(self, request, *args, **kwargs):
        RefreshToken.objects.filter(user=request.user).delete()

        return Response(status=status.HTTP_204_NO_CONTENT)
