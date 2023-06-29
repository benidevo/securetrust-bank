#!/bin/bash

set -o errexit
set -o pipefail
set -o nounset

MYSQL_HOST="${MYSQL_HOST:-bank-account-db}"
MYSQL_USER="${MYSQL_USER:-root}"
MYSQL_PORT="${MYSQL_PORT:-3306}"
MYSQL_PASSWORD="${MYSQL_PASSWORD:-mysecretpassword}"
RABBITMQ_HOST="${RABBITMQ_HOST:-rabbitmq}"
RABBITMQ_PORT="${RABBITMQ_PORT:-5672}"

# wait for RabbitMQ
echo "Waiting for RabbitMQ..."
while ! nc -z "$RABBITMQ_HOST" "$RABBITMQ_PORT"; do
  sleep 0.1
done
echo "RabbitMQ is now available"

check_mysql() {
    echo "Checking if MySQL is ready..."
    until mysqladmin ping -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" --silent; do
        echo "MySQL is not ready yet. Retrying in 5 seconds..."
        sleep 5
    done
    echo "MySQL is now ready."
}

check_mysql

echo "Starting the Spring Boot application..."

./mvnw spring-boot:run
