build:
	docker compose up --build -d --remove-orphans

up:
	docker-compose up -d

down:
	docker-compose down

show-logs-api_gateway:
	docker compose logs api-gateway

show-logs-user_account:
	docker compose logs user-account-service

show-logs-user_account_db:
	docker compose logs user-account-db

user-account-db-generate_migrations:
	docker compose exec -it user-account-service python manage.py makemigrations


test-user-account:
	docker compose exec -it user-account-service pytest

user-account-db-migrate:
	docker compose exec -it user-account-service python manage.py migrate

show-logs-redis:
	docker compose logs redis
show-logs-notification-service:
	docker compose logs notification-service

show-logs-notification-service_errors:
	docker compose exec -it notification-service tail -f logs/error.log

show-logs-file-upload-service:
	docker compose logs file-upload-service

show-logs-rabbitmq:
	docker compose logs rabbitmq

redis-cli:
	docker-compose exec redis redis-cli

superuser:
	docker compose exec -it user-account-service python manage.py createsuperuser
