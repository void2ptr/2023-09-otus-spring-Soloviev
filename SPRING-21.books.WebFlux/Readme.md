- [SPRING-21](https://github.com/void2ptr/2023-09-otus-spring-Soloviev/tree/main/SPRING-21.books.WebFlux) - Spring WebFlux

## Домашнее задание
Использовать WebFlux

## Цель:
- Цель: разрабатывать Responsive и Resilent приложения на реактивном стеке Spring c помощью Spring Web Flux и Reactive Spring Data Repositories
- Результат: приложение на реактивном стеке Spring


## Описание/Пошаговая инструкция выполнения домашнего задания:
- За основу для выполнения работы можно взять ДЗ с Ajax + REST (классическое веб-приложение для этого луче не использовать).
- Но можно выбрать другую доменную модель (не библиотеку).
- Необходимо использовать Reactive Spring Data Repositories.
- В качестве БД лучше использовать MongoDB или Redis. Использовать PostgreSQL и экспериментальную R2DBC не рекомендуется.
- RxJava vs Project Reactor - на Ваш вкус.
- Вместо классического Spring MVC и embedded Web-сервера использовать WebFlux.
- Опционально: реализовать на Functional Endpoints
- Данное задание НЕ засчитывает предыдущие!
### Рекомендации:
Старайтесь избавиться от лишних архитектурных слоёв. Самый простой вариант - весь flow в контроллере
