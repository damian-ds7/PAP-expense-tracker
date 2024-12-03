#!/bin/bash

(
    cd ./ExpenseAPI
    ./mvnw clean spring-boot:run >> /dev/null &
)

BACKEND_PID=$!


echo -n "Waiting for backend server to start..."
until $(curl --output /dev/null --silent --head --fail http://localhost:8080/expense/recent); do
    printf '.'
    sleep 1
done

echo -e "\nBackend server is up. Starting frontend..."

(
    cd ./frontend
    ./gradlew run
)

kill $BACKEND_PID