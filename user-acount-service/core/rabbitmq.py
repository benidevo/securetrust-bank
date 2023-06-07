import pika
from django.conf import settings


class RabbitMQClient:
    HOST = settings.RABBITMQ_HOST
    USERNAME = settings.RABBITMQ_USER
    PASSWORD = settings.RABBITMQ_PASS
    PORT = 5672

    def __init__(self):
        self.host = self.HOST
        self.port = self.PORT
        self.username = self.USERNAME
        self.password = self.PASSWORD
        self.virtual_host = "/"
        self.connection = None

    def connect(self):
        credentials = pika.PlainCredentials(self.username, self.password)
        parameters = pika.ConnectionParameters(
            host=self.host,
            port=self.port,
            virtual_host=self.virtual_host,
            credentials=credentials,
        )
        self.connection = pika.BlockingConnection(parameters)

    def disconnect(self):
        if self.connection and not self.connection.is_closed:
            self.connection.close()

    def publish_message(self, queue, message):
        if not self.connection or self.connection.is_closed:
            self.connect()

        channel = self.connection.channel()
        channel.queue_declare(queue=queue, durable=True)

        properties = pika.BasicProperties(delivery_mode=2)
        channel.basic_publish(
            exchange="", routing_key=queue, body=message, properties=properties
        )

    def __enter__(self):
        self.connect()
        return self

    def __exit__(self, exc_type, exc_val, exc_tb):
        self.disconnect()
