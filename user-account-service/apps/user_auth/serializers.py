from django.contrib.auth import get_user_model
from django.contrib.auth.password_validation import validate_password
from rest_framework import serializers

User = get_user_model()


class UserRegistrationSerializer(serializers.ModelSerializer):
    password = serializers.CharField(write_only=True)

    class Meta:
        model = User
        fields = ("email", "first_name", "last_name", "password")

    def validate_password(self, password):
        validate_password(password)
        return password

    def create(self, validated_data):
        password = validated_data.pop("password")
        user = User(**validated_data)
        user.set_password(password)
        user.save()
        return user


class EmailSerializer(serializers.Serializer):
    email = serializers.EmailField()


class OTPSerializer(serializers.Serializer):
    otp = serializers.CharField(max_length=6, min_length=6)


class VerifyEmailSerializer(EmailSerializer, OTPSerializer):
    pass


class ResetPasswordSerializer(EmailSerializer, OTPSerializer):
    password = serializers.CharField(write_only=True)

    def validate_password(self, password):
        validate_password(password)
        return password


class LoginSerializer(EmailSerializer):
    password = serializers.CharField(write_only=True)

    def validate_password(self, password):
        validate_password(password)
        return password


class RefreshTokenSerializer(serializers.Serializer):
    refresh_token = serializers.CharField(max_length=2000)
