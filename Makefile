
.PHONY: up down

up:
	docker compose run -d -p "8080:8080" bank-api ./gradlew clean build bootRun -x test

down:
	docker compose down --remove-orphans
