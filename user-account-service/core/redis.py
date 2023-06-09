import json

from django.core.cache import cache


class Cache:
    def set(self, key, value, ttl=3600):
        value = json.dumps(value)
        cache.set(key, value, ttl)

    def get(self, key):
        data = cache.get(key)
        return json.loads(data) if data else None

    def delete(self, key):
        cache.delete(key)

    def flush(self):
        cache.clear()

    def expiry_time(self, key):
        return cache.ttl(key)

    def set_expiry(self, key, ttl):
        cache.expire(key, ttl)
