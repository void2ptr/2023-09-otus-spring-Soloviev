# For hand test only

# Authors
curl  -X "GET"    http://localhost:8080/api/v1/authors
curl  -X "GET"    http://localhost:8080/api/v1/authors/1
curl --header "Content-Type: application/json" --request POST --data '{"id":0,"fullName":"The_Author"}'  http://localhost:8080/api/v1/authors
curl --header "Content-Type: application/json" --request PUT  --data '{"id":17,"fullName":"The-Author"}' http://localhost:8080/api/v1/authors/17
curl  -X "DELETE" http://localhost:8080/api/v1/authors/17


curl  -X "GET"    http://localhost:8080/func/authors

# Genres
curl  -X "GET"    http://localhost:8080/api/v1/genres
curl  -X "GET"    http://localhost:8080/api/v1/genres/1
curl --header "Content-Type: application/json" --request POST --data '{"id":0,"name":"TEST ADDED"}' http://localhost:8080/api/v1/genres
curl --header "Content-Type: application/json" --request PUT  --data '{"id":22,"name":"TEST EDIT"}' http://localhost:8080/api/v1/genres/22

curl  -X "DELETE" http://localhost:8080/api/v1/genres/22

# Books
curl  -X "GET"    http://localhost:8080/api/v1/books

# Comments
curl  -X "GET" http://localhost:8080/api/v1/books/4/comments