# microservices-example
This project make use of spring cloud services &amp; docker to build different PoCs

Demonstrated concepts:

* Integration testing using Docker
* Service discovery
* API gateway
* Currency Converter

## Docker

Each service is built and deployed using Docker. End-to-end integration testing can be done on a developer's machine using Docker compose.

## Service discovery

This project contains two discovery services, one on Netflix Eureka, and the other uses Consul from Hashicorp. Having multiple discovery services provides the opportunity to use one (Consul) as a DNS provider for the cluster, and the other (Eureka) as a proxy-based API gateway.

## API gateway

Each microservice will coordinate with Eureka to retrieve API routes for the entire cluster. Using this strategy each microservice in a cluster can be load balanced and exposed through one API gateway. Each service will automatically discover and route API requests to the service that owns the route. This proxying technique is equally helpful when developing user interfaces, as the full API of the platform is available through its own host as a proxy.

## Currency Converter

### Intro

Implement a server­side Java currency converter, which converts Euro, Dollars and Yen 
in all directions. Please Expose your service in a RESTful API. Use a common 
framework for your business logic. Please use exchange rates from a publicly available 
API.

### Access

http://localhost:8765/currencyconverter/rates?from=<currency_from>&to=<currency_to>&value=<value>

### Notice

- Endpoint is unsecured
- Code should be optimised for prod
- This is a PoC !

### Video

[Currency Converter Example](https://www.youtube.com/watch?v=3B1OduROSfg)

# References

Inspired for time to deployment reasons by [Building Microservices with Spring Cloud and Docker](http://www.kennybastani.com/2015/07/spring-cloud-docker-microservices.html)

# License

This project is licensed under Apache License 2.0.

