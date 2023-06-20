#!/bin/bash

if [ -z "$MYSQL_HOST" ]; then
  MYSQL_HOST="bank-account-db"
fi

if [ -z "$MYSQL_PORT" ]; then
  MYSQL_PORT="3306"
fi

if [ -z "$MYSQL_USER" ]; then
  MYSQL_USER="root"
fi

if [ -z "$MYSQL_PASSWORD" ]; then
  MYSQL_PASSWORD="mysecretpassword"
fi

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