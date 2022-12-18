# SmartHome

## How to start

### Prerequisites

- have jdk 11 set for the project
- have docker installed
- have npm installed

## Local Development

### Starting Databases and Keycloak

Run [basic-docker-compose](./docker/compose/basic-docker-compose.yml). This file start keycloak (both it, and it's database)
and the App database.

### Starting the frontend

Go to the [frontend folder](./frontend) and run the command:
`npm start`

This will start the app on `localhost:3000`.

### Starting the Backends

Go to the selected service, and run the spring app. In Intellij IDEA, go to the main class and run it
using the green triangle.

### Running the Simulators

Run the same way as Backends

### Database schema and test data

Data-Service has been configured to recreate the database on each start. It will recreate all tables and add test data.
Remember to update [schema and data!](./Data-Service/src/main/resources)

### Port List

For local development:

- Frontend: 3000

- Control Service: 8080
- Data Service: 8081
- App Database: 5432

- Air Humidifier Sim: 8082
- Air Conditioning Sim: 8083
- Air Filter Sim: 8084

- Keycloak: 8180
- Keycloak Database: 5433

### Apis

The 2 backend services each have their own base path:
* Control Service: /api/control
* Data Service: /api/data

These apis are set in the app properties for each service.

Keycloak does not have a base path. All url can be found in the keycloak documentation.

## Starting all on docker

Each of the services have a corresponding Dockerfile. They can all be started at once using the
[complete-docker-compose file](./docker/compose/complete-docker-compose.yml). The ports are the same as in local development
([see here](#port-list)).

Please note: the images take a while to create

### Environment Variables and Urls

When using the complete docker file, all non-frontend env variables are set for docker network.
When communicating with the containers from the host machine, use localhost.
