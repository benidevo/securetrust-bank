from django.contrib.auth import get_user_model
from rest_framework import serializers

from apps.users.models import Profile

User = get_user_model()


class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profile
        fields = [
            "id",
            "avatar_url",
            "gender",
            "phone_number",
            "address",
            "city",
            "country",
            "date_of_birth",
            "nin",
        ]


class UserSerializer(serializers.ModelSerializer):
    profile = UserProfileSerializer()

    class Meta:
        model = User
        fields = [
            "id",
            "email",
            "first_name",
            "last_name",
            "role",
            "is_active",
            "profile",
            "created_at",
            "updated_at",
        ]

    def update(self, instance, validated_data):
        profile_data = validated_data.pop("profile", None)

        if profile_data:
            profile_serializer = self.fields["profile"]
            profile_instance = instance.profile
            profile = profile_serializer.update(profile_instance, profile_data)
            instance.profile = profile

        instance = super().update(instance, validated_data)

        return instance
