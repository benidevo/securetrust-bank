from django.contrib import admin
from django.urls import include, path
from drf_yasg import openapi
from drf_yasg.views import get_schema_view
from rest_framework import permissions

schema_view = get_schema_view(
    openapi.Info(
        title="SecureTrust Bank API",
        default_version="v1",
        description="SecureTrust Bank is an online banking platform that allows users to create bank accounts, send and receive money securely.",  # noqa
        contact=openapi.Contact(email="benjaminidewor@gmail.com"),
        license=openapi.License(name="MIT License"),
    ),
    public=True,
    permission_classes=(permissions.AllowAny,),
)

urlpatterns = [
    path("api/v1/user-account/api-docs", schema_view.with_ui("redoc", cache_timeout=0)),
    path("api/v1/user-account/admin/", admin.site.urls),
    path("api/v1/auth/", include("apps.user_auth.urls")),
]
