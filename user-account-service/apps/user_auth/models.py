from django.contrib.auth import get_user_model
from django.db import models
from django.utils import timezone
from django.utils.translation import gettext_lazy as _

User = get_user_model()


class RefreshToken(models.Model):
    user = models.OneToOneField(
        User, on_delete=models.CASCADE, related_name="refresh_token"
    )
    token = models.TextField(unique=True, verbose_name=_("token"))
    is_active = models.BooleanField(verbose_name=_("is_active"), default=True)
    created = models.DateTimeField(default=timezone.now, verbose_name=_("created"))

    class Meta:
        ordering = ["-created"]

    def __str__(self):
        return f"{self.user.first_name}'s token"
