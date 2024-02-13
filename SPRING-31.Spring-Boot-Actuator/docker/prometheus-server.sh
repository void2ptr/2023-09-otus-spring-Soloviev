#!/bin/sh

cd ..
root=`pwd`

echo  $root/target/classes/prometheus.yml

# Create persistent volume for your data
docker volume create prometheus-data

## Start Prometheus container
docker run -td -p 9095:9090 --name prometheus1 -v prometheus-data:/prometheus prom/prometheus

