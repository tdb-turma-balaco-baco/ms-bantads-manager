# ms-bantads-manager

The Bantads project is part of a college assignment, with the objective of implementing
a microservices based architecture, with various technologies, such as
Java, Spring Boot, Nodejs, Docker, PostgreSQL, etc.

This repository contains the manager microservice.

## Requirements and Usage

To start the application, you must have the minimum requirements:

- Java 21
- Docker

With this, you can now follow these steps:

First of all, clone the repo.
```bash
$ git clone git@github.com:tdb-turma-balaco-baco/ms-bantads-manager.git
```

Then, execute the `docker-compose.yml` file.
```bash
$ cd ms-bantads-manager
$ docker compose up -d
```

Now, you can start the Spring Boot application within your IDE of choice, or
by execute the `maven` goal.
```bash
$ mvn spring-boot:run -f pom.xml
```

## Tests

To run all the tests, execute the following `maven` goal.
```bash
mvn test -f pom.xml
```
