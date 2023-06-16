from django.urls import path

from apps.users.views import UserDetailView, UserView

urlpatterns = [
    path("", UserView.as_view(), name="users-view"),
    path("<uuid:user_id>", UserDetailView.as_view(), name="user-detail-view"),
]
