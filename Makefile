up:
	docker-compose up -d

down:
	docker-compose down

show-logs-user_account_db:
	docker compose logs user-account-db

show-logs-redis:
	docker compose logs redis

show-logs-rabbitmq:
	docker compose logs rabbitmq

redis-cli:
	docker-compose exec redis redis-cli
