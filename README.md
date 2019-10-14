# money-transfer api

Tech stack:

Java 11, gradle, javalin framework, jackson, lombok, slf4j, junit, mockito, powermock

-------------------------------------


Instaliation:


In project directory execute 
./gradlew clean jar


java -jar money-transfer-1.0-SNAPSHOT.jar


-------------------------------------

Rest API:

POST - create account

http://localhost:7000/account/

Body:

{
	"id": 1,
	"balance": 300.99,
	"currency": "USD"
}


-------------------------------------

GET - get account by id

http://localhost:7000/account/:id

-------------------------------------

GET - get all accounts

http://localhost:7000/account/all

-------------------------------------

POST - provide money transfers between accounts

http://localhost:7000/transfer

{
	"sourceAccountId": 2,
	"destinationAccountId": 3,
	"amount": 7.54,
	"currency": "USD"
}

-------------------------------------

GET - get all tansactions

http://localhost:7000/transaction/all




