from django.contrib.auth import get_user_model
from django_elasticsearch_dsl import Document, fields
from django_elasticsearch_dsl.registries import registry

User = get_user_model()


@registry.register_document
class UserDocument(Document):
    email = fields.TextField()
    is_active = fields.BooleanField()
    nin = fields.TextField()
    phone = fields.TextField()

    class Index:
        name = "users"
        settings = {"number_of_shards": 1, "number_of_replicas": 0}

    class Django:
        model = User

    def prepare_phone(self, instance):
        profile = getattr(instance, "profile", None)
        if profile and profile.phone_number:
            return str(instance.profile.phone_number)
        return None

    def prepare_nin(self, instance):
        profile = getattr(instance, "profile", None)
        if profile and profile.nin:
            return instance.profile.nin
        return None
