#!/bin/bash

set -o errexit

set -o pipefail

set -o nounset

echo "Waiting For RabbitMQ..."
while ! nc -z rabbitmq 5672; do
  sleep 0.1
done
echo "RabbitMQ Is Now Available"

echo "Waiting For Email Server..."
while ! nc -z email-server 8025; do
  sleep 0.1
done
echo "Email Server Is Now Available"

status=$?
if [ $status -eq 0 ]; then
    echo "Initializing Notification Service"

    start_command="npm run dev"

    exec $start_command
else
  echo "Error Initializing Notification Service, Exiting..."
fi
