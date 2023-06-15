#!/bin/bash

set -o errexit

set -o pipefail

set -o nounset

echo "Waiting For AWS S3..."
while ! nc -z aws-emulator 4566; do
  sleep 0.1
done
echo "AWS S3 Is Now Available"

echo "Waiting For RabbitMQ..."
while ! nc -z rabbitmq 5672; do
  sleep 0.1
done

status=$?
if [ $status -eq 0 ]; then
    echo "Initializing File Upload Service"

    start_command="npm run dev"

    exec $start_command
else
  echo "Error Initializing File Upload Service, Exiting..."
fi
