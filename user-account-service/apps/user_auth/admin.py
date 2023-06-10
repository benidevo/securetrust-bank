from django.contrib import admin

from apps.user_auth.models import RefreshToken


class RefreshTokenAdmin(admin.ModelAdmin):
    list_display = ["id", "user", "token", "is_active"]
    list_display_links = ["id", "token"]
    list_filter = ["id", "is_active", "created"]


admin.site.register(RefreshToken, RefreshTokenAdmin)
