from django.contrib import admin
from django.urls import include, path

urlpatterns = [
    path("user-account/admin/", admin.site.urls),
    path("api/v1/auth/", include("apps.user_auth.urls")),
]
