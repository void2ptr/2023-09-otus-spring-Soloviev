#!/bin/sh

docker system df
echo --container------------------------------------------------------
docker container ls
echo --image----------------------------------------------------------
docker image     ls
echo --volume---------------------------------------------------------
docker volume    ls
echo ------------------------------------------------------------------

#docker-compose ps
#docker-compose stop postgres
#docker-compose rm --force postgres
#docker volume ls
#docker volume rm postgres
#docker-compose up --force-recreate -d postgres

#docker-compose stop prometheus
#docker-compose rm --force prometheus
#docker image rm ad37434ef428
