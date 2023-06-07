from django.contrib.auth.base_user import BaseUserManager
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.utils.translation import gettext_lazy as _


class CustomUserManager(BaseUserManager):
    def email_validator(self, email):
        try:
            validate_email(email)
            return True
        except ValidationError:
            raise ValueError(_("You must provide a valid email address"))

    def create_user(self, first_name, last_name, email, password, **extra_fields):
        if not first_name:
            raise ValueError(_("First name must be provided"))
        if not last_name:
            raise ValueError(_("Last name must be provided"))
        if not email:
            raise ValueError(_("Email must be provided"))
        else:
            email = self.normalize_email(email)
            self.email_validator(email)

        user = self.model(
            first_name=first_name, last_name=last_name, email=email, **extra_fields
        )
        user.set_password(password)

        extra_fields.setdefault("is_staff", False)
        extra_fields.setdefault("is_superuser", False)

        user.save(using=self._db)
        return user

    def create_superuser(self, first_name, last_name, email, password, **extra_fields):
        extra_fields.setdefault("is_staff", True)
        extra_fields.setdefault("is_superuser", True)
        extra_fields.setdefault("is_active", True)

        if not password:
            raise ValueError(_("Superuser password must be provided"))

        if not email:
            raise ValueError(_("Superuser email must be provided"))
        else:
            email = self.normalize_email(email)
            self.email_validator(email)

        return self.create_user(
            first_name=first_name,
            last_name=last_name,
            email=email,
            password=password,
            **extra_fields
        )
