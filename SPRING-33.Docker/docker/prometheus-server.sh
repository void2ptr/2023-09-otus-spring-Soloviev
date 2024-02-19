#!/bin/sh

cd ..
root=`pwd`

## Start Prometheus container
docker run -td -p 9095:9090 --name prometheus1 -v prometheus-data:/prometheus prom/prometheus
