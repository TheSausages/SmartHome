# SmartHome

## How to start

### Prerequisites

- have jdk 11 set for the project
- have docker installed
- have npm installed

### Starting Databases and Keycloak

Run [docker-compose](./docker/compose/basic-docker-compose.yml). This file start keycloak (both it, and it's database)
and the App database.

### Starting the frontend

Go to the [frontend folder](./frontend) and run the command:
`npm start`

This will start the app on `localhost:3000`.

### Starting the Backends

Go to the selected service, and run the spring app. In Intellij IDEA, go to the main class and run it
using the green triangle.


### Database schema and test data

Data-Service has been configured to recreate the database on each start. It will recreate all tables and add test data.
Remember to update [schema and data!](./Data-Service/src/main/resources)
