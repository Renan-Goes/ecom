# ecom
The API was developed for a test, the idea is for a user to add products for sale, check the products, whether all of them, by name or by user.
The user can also buy the products of other users. All of the products are registered in an external API which can be found in the following link:
https://github.com/Renan-Goes/products-manager/

## How to run
To run the project, you only have to run the command "docker-compose up" in the root directory of the project.

The documentation can also be found using Swagger through the path: "http://localhost:8081/swagger-ui/index.html#/".

In other to run the Integration tests, the external API must be up, it only needs to be run by using "mvn spring-boot:run" in the root directory of the project. The product-manager project can be found here: https://github.com/Renan-Goes/products-manager

## What was used for the project?
The application was developed using the following technologies:
- Spring boot
- Maven
- JWT
- Swagger
- MySQL 8
- JUnit
- PowerMock
- FlyWay
