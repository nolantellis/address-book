#!/bin/sh

./gradlew clean bu bJ

docker build -t myapp/addressbook-app .

docker-compose up