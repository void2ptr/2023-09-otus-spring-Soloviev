#!/bin/sh

# Create persistent volume for your data
#docker volume create spring-batch-data

# port: 5430 for DEV only !
docker run --rm \
       --name pg-docker-dev \
       -e POSTGRES_PASSWORD=postgres \
       -e POSTGRES_USER=postgres \
       -e POSTGRES_DB=demoDB \
       -p 5430:5432 \
       postgres:15
