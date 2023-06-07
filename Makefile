up:
	docker-compose up -d

down:
	docker-compose down

redis-cli:
	docker-compose exec redis redis-cli
