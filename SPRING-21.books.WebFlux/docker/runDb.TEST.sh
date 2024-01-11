#!/bin/sh

# port: 5440 for TEST only !
docker run --rm --name pg-docker-test \
       -e POSTGRES_PASSWORD=pwd \
       -e POSTGRES_USER=usr \
       -e POSTGRES_DB=testDB \
       -p 5440:5432 \
       postgres:15
