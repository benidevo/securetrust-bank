#!/bin/bash

set -o errexit
set -o pipefail
set -o nounset

current_directory=$(pwd)

cp "$current_directory/env.example" "$current_directory/.env"
echo "Created '$current_directory/.env' file."

for dir in "$current_directory"/*/; do
  directory_name=${dir%*/}
  base_directory=${directory_name##*/}

  if [ -f "$directory_name/env.example" ]; then
    cp "$directory_name/env.example" "$directory_name/.env"
    echo "Created '$directory_name/.env' file."
  fi
done

read -p "Do you want to start the containers? (y/n): " start_containers

if [ "$start_containers" = "y" ]; then
    echo "Containers starting..."
    rm -rf file-upload-service/node_modules
    rm -rf notification-service/node_modules
    docker-compose up --build -d --remove-orphans
    echo "Containers started."

    read -p "Do you also want to install local dependencies? (y/n): " install_deps
    if [ "$install_deps" = "y" ]; then
        cd file-upload-service && echo "Installing file-upload-service dependencies..." && npm i && cd ..
        sleep 10
        echo "file-upload-service dependencies installed."

        cd notification-service && echo "Installing notification-service dependencies..." && npm i && cd ..
        sleep 10
        echo "notification-service dependencies installed."

        echo "Installing user-account-service dependencies..."
        rm -rf venv
        python3 -m venv venv
        source venv/bin/activate
        pip install --upgrade pip
        pip install -r user-account-service/requirements/local.txt
        echo "user-account-service dependencies installed."
    fi
else
    echo "Containers not started."
    exit 0
fi
echo "Done."