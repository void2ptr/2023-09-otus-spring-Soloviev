# ДЗ 37
## Setup app
```shell
mvn clean install
```

## Использование Resilent4j
```shell
cd ./books-rest-servers
```
### Link for server:
- Server listen     on: http://localhost:8080
- Actuator root       : http://localhost:8090/actuator
- Actuator Health     : http://localhost:8090/actuator/health
- Actuator Logfile    : http://localhost:8090/actuator/logfile
- Prometheus: metrics : http://localhost:8090/actuator/prometheus
- Prometheus: GUI     : http://localhost:9095/graph
- HATEOAS             : http://localhost:8080/hateoas/api
- Grafana             : http://localhost:3000
- swagger: ui         : http://localhost:8080/swagger-ui/index.html
- swagger: api-docs   : http://localhost:8080/v3/api-docs

## Использование Feign Client
```shell
cd ./books-feign-client
```
### Link for clients:
- Server listen   on: http://localhost:8981
- swagger: ui       : http://localhost:8981/swagger-ui/index.html
- swagger: api-docs : http://localhost:8981/v3/api-docs
