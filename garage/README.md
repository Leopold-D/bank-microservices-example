# Garage

## Intro

The city of Vence needs help with the 

implementation of a new parking garage and 

asks you for your support.

The garage should support different types of 

vehicles:


• Cars

• Motorbikes


Every vehicle has a unique identifier (the licence plate), and can exist 

only once – thus being either within the garage or outside of it.

The planned garage should support multiple parking levels – the city of 

Vence is currently undecided how high they will be able to build for 

stability reasons. As a result, your implementation should allow for 

arbitrary numbers of parking levels – at least 1 level, but keep it flexible.

The same goes for the number of parking spaces per level – the area 

where the garage will be built is not yet decided upon. So again, keep 

this flexible and configurable.


Your task is to develop a simulation program for the garage. Vehicles 

should be able to enter and exit the garage – the garage should then 

assign a free space or reject the vehicle if there are no more free parking 

lots.


The manager of the garage should be able to ask the system for the 

location of a specific vehicle. The response should include the parking 

level and the assigned parking lot. Also, the number of free parking lots 

should be queryable.

## Implementation

- Split into 3 services (API, API Admin, CORE) providing shells and PoC for what could be a deployable Garage Management service
- Shared project contains shared entities to avoid duplication
- Only API and Admin API are exposed, CORE service is available only for localhost
- Admin API handle OAuth2 authenticated calls while API handles non authenticated calls
- MongoDB has been used as a PoC for DB management (url : mongodb://<dbuser>:<dbpassword>@ds013579.mlab.com:13579/garage)

## Video

[Garage Video](http://youtu.be/7ezeuhtfY4I?hd=1)

## Testing

- Base API tests are presents, testing @API level. SOAPUI is required to run them
- Units tests are present for the relevant, none workflow dependent methods (ie. Checking if a registration plate is valid).

## Coding rules

Applied when possible (dto and db excluded).

Prepend :
a for class variables
p for parameter
l for local variable
m for method

CamelCase is used

## Sub-Levels ReadMe

[Garage API](https://github.com/Leopold-D/microservices-example/blob/garage/garage/garage-api-microservice/README.md)

[Garage Admin API](https://github.com/Leopold-D/microservices-example/blob/garage/garage/garage-admin-api-microservice/README.md)

[Garage Core](https://github.com/Leopold-D/microservices-example/blob/garage/garage/garage-core-microservice/README.md)

[Garage Shared](https://github.com/Leopold-D/microservices-example/blob/garage/garage/garage-shared/README.md)