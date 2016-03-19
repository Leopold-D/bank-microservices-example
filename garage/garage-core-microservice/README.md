# Garage Core microservice

## Intro

This service is the core of the garage project, it computes the answer to send the client based on the different services input.

## Implementation

- MongoDB used for database, through an interface to keep it flexible
- Sticking to standard HTTP return codes as much as possible
- Levels can be modified but lots need to be free to reduce the size, so it's better to set the level as not in use and then wait the car to go out to change a level size
- Only last level can be deleted

## DB Considerations

- Service used is https://mlab.com/
- Connection details are available in the MongoDBConnector class
- 2 objects family are stored: Levels and Vehicles