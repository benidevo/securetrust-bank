from django.urls import path

from apps.user_auth.views.registration import (
    RegisterView,
    ResendEmailVerificationView,
    VerifyEmailView,
)

urlpatterns = [
    path("register", RegisterView.as_view(), name="user-register"),
    path("verify-email", VerifyEmailView.as_view(), name="verify-email"),
    path(
        "resend-verification",
        ResendEmailVerificationView.as_view(),
        name="resend-verification",
    ),
]
