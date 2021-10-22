# ecom
The API was developed for a test, the idea is for a user to add products for sale, check the products, whether all of them, by name or by user.
The user can also buy the products of other users. All of the products are registered in an external API which can be found in the following link:
https://github.com/Renan-Goes/products-manager/

The documentation can also be found using Swagger through the endpoint: "http://localhost:8081/swagger-ui/index.html#/".

## Creating User
To create the user, the endpoint "http://localhost:8081/auth/signup" can be used with the method POST, along with a json with the fields: "email", "password" and "userName".
The username and email must be unique and you cannot create a user with an already existing email or userName. Also the email must be valid and there are a minimum
and maximum of characters for "password" and "userName"

## Logging In
An user can Log In through the endpoint "http://localhost:8081/auth/signin" with the method POST, using a json with the fields "email" and "password", with the email of 
an existing account and its corresponding password, the user can be logged in and will begiven a token for 2 hours, which is required for all other functionalities.

## Adding a product for sale
If an user desires to put a product for sale, he can POST to the endpoint "http://localhost:8081/addproduct", with a json with fields "name", "price" and "description".
"name" and "description" must have a minimum and maximum of characters, and "price" must be at least 0.01. The product will be given an unique ID which will be necessary for other functionalities.

## Check all products
To check all the products for sale an user can use the endpoint "http://localhost:8081/products" with the GET method, which will exibit all products with their informations in a page.
By adding "?page={page number}&quantity={number of products per page}", the user can see a specific number of products per page in a given page.

## Check products by user
Through the endpoint "http://localhost:8081/products/{username}" the user can check all products of a specific user in a page.

## Check products by name
If the user wishes to search for a product that contains, e.g. "bat" in the name, it can return a page with all products with "bat" in its name, like "battery" or "baseball bat".
That can be done through the endpoint "http://localhost:8081/products/{username}"

## Update product
If a user wants to update a product of its own, it can use the endpoint with the PATCH method along with a json with the field "value" containing any of the fields used in the product (except for ID).
The product will then change the values of the fields that were passed through the endpoint "http://localhost:8081/updateproduct/{productId}", where "productId" can be found by searching the products.

## Buy products
With a POST method in the endpoint "http://localhost:8081/buy", along with the field "listOfProductIds" as list of product ID's, the user can buy the products of other users.

## Delete account
If the user wants to delete its account, it can use the endpoint "http://localhost:8081/auth/deleteaccount", if the token is correct, the user will be deleted along the products it has for sale.
