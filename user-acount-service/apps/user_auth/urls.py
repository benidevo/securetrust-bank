from django.urls import path

from apps.user_auth.views import RegisterView

urlpatterns = [path("register", RegisterView.as_view(), name="user-register")]
