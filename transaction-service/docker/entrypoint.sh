#!/bin/bash

set -o errexit
set -o pipefail
set -o nounset

MONGODB_HOST="${MONGODB_HOST:-transaction-db}"
MONGODB_PORT="${MONGODB_PORT:-27017}"
RABBITMQ_HOST="${RABBITMQ_HOST:-rabbitmq}"
RABBITMQ_PORT="${RABBITMQ_PORT:-5672}"

# wait for RabbitMQ
echo "Waiting for RabbitMQ..."
while ! nc -z "$RABBITMQ_HOST" "$RABBITMQ_PORT"; do
  sleep 0.1
done
echo "RabbitMQ is now available"

# wait for mongodb
echo "Waiting for Transaction DB..."
while ! nc -z "$MONGODB_HOST" "$MONGODB_PORT"; do
  sleep 0.1
done
echo "Transaction DB is now available"

echo "Starting the Spring Boot application..."

./mvnw spring-boot:run
