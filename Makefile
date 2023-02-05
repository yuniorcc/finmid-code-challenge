
.PHONY: up down test

up:
	docker compose run -d -p "8080:8080" bank-api gradle clean build bootRun -x test

down:
	docker compose down --remove-orphans

test:
	docker compose run --rm -p "8080:8080" bank-api gradle clean build test
