from django.urls import path

from apps.user_auth.views.login import LoginView
from apps.user_auth.views.logout import LogoutView
from apps.user_auth.views.password import ForgotPasswordView, ResetPasswordView
from apps.user_auth.views.registration import (
    RegisterView,
    ResendEmailVerificationView,
    VerifyEmailView,
)
from apps.user_auth.views.token import DeleteRefreshToken, RefreshTokenView

urlpatterns = [
    path("register", RegisterView.as_view(), name="user-register"),
    path("verify-email", VerifyEmailView.as_view(), name="verify-email"),
    path(
        "resend-verification",
        ResendEmailVerificationView.as_view(),
        name="resend-verification",
    ),
    path("forgot-password", ForgotPasswordView.as_view(), name="forgot-password"),
    path("reset-password", ResetPasswordView.as_view(), name="reset-password"),
    path("login", LoginView.as_view(), name="login"),
    path("logout", LogoutView.as_view(), name="logout"),
    path("token", DeleteRefreshToken.as_view(), name="delete-refresh-token"),
    path("token/refresh", RefreshTokenView.as_view(), name="refresh-token"),
]
