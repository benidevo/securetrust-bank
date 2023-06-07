from uuid import uuid4 as uuid

from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin
from django.core import validators
from django.db import models
from django.utils.translation import gettext_lazy as _
from django_countries.fields import CountryField
from phonenumber_field.modelfields import PhoneNumberField

from apps.users.managers import CustomUserManager


class User(AbstractBaseUser, PermissionsMixin):
    pkid = models.BigAutoField(primary_key=True, editable=False)
    id = models.UUIDField(default=uuid, editable=False, unique=True)
    email = models.EmailField(
        verbose_name=_("Email Address"), unique=True, max_length=100
    )
    first_name = models.CharField(_("First Name"), max_length=50)
    last_name = models.CharField(_("Last Name"), max_length=50)
    is_active = models.BooleanField(_("Active"), default=False)
    is_staff = models.BooleanField(_("Staff"), default=False)
    created_at = models.DateTimeField(_("Created At"), auto_now_add=True)
    updated_at = models.DateTimeField(_("Updated At"), auto_now=True)

    USERNAME_FIELD = "email"
    REQUIRED_FIELDS = ["first_name", "last_name"]

    class Meta:
        verbose_name = _("user")
        verbose_name_plural = _("users")
        ordering = ["-created_at", "-updated_at"]

    objects = CustomUserManager()

    def __str__(self):
        return self.first_name

    @property
    def full_name(self):
        return f"{self.first_name} {self.last_name}"


class Profile(models.Model):
    class Gender(models.TextChoices):
        MALE = (
            "M",
            _("Male"),
        )
        FEMALE = (
            "F",
            _("Female"),
        )
        OTHER = (
            "O",
            _("Other"),
        )

    pkid = models.BigAutoField(primary_key=True, editable=False)
    id = models.UUIDField(default=uuid, editable=False, unique=True)
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name="profile")
    avatar_url = models.URLField(blank=True, null=True, verbose_name=_("avatar"))
    gender = models.CharField(
        verbose_name=_("Gender"),
        max_length=20,
        choices=Gender.choices,
        default=Gender.OTHER,
    )
    phone_number = PhoneNumberField(
        verbose_name=_("Phone Number"),
        max_length=18,
        unique=True,
        blank=True,
        null=True,
    )
    address = models.CharField(
        verbose_name=_("Address"), default="", blank=True, max_length=255
    )
    city = models.CharField(
        verbose_name=_("City"), default="Lagos", blank=True, null=True, max_length=20
    )
    country = CountryField(
        verbose_name=_("Country"), default="NGA", blank=True, null=True
    )
    date_of_birth = models.DateField(_("Date of Birth"), blank=True, null=True)
    nin = models.IntegerField(
        _("National Identification Number"),
        blank=True,
        null=True,
        validators=[
            validators.MaxValueValidator(99999999999),
            validators.MinValueValidator(10000000000),
        ],
    )
    created_at = models.DateTimeField(_("Created At"), auto_now_add=True)
    updated_at = models.DateTimeField(_("Updated At"), auto_now=True)

    class Meta:
        ordering = ["created_at", "updated_at"]

    def __str__(self):
        return f"{self.user.first_name}'s Profile"

    @property
    def is_complete(self):
        field_values = [
            self.gender,
            self.phone_number,
            self.address,
            self.city,
            self.country,
            self.date_of_birth,
            self.nin,
        ]
        return all(field_values)
