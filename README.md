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

## Selenium Tests

Selenium tests can be run standalone by running the [Selenium Tests](./Selenium-Tests/src/test).
Requirements:
* Having Chrome installed
* Having all containers from [docker compose complete](./docker/compose/complete-docker-compose.yml) running
  * NOTE: they must be from this compose, as necessary environment variables are set
* Run tests from [Selenium Test Folder](./Selenium-Tests/src/test)

\
\
In order to run selenium tests in docker, you need:
* Run the [Selenium Docker Compose](./docker/compose/selenium-docker-compose.yml)
  * NOTE: must be this one! 
* When all of them are running, go to [Selenum-Tests test section](./Selenium-Tests/src/test)
* Run the tests:
  * with 2 additional environmental properties (they can also be found [here](./Selenium-Tests/src/main/resources/application-docker.properties)):
    * grid.url=http://localhost:4444
    * frontend.url=http://frontend:800
  * change the file name in [Base Selenium Test](./Selenium-Tests/src/test/java/pwr/smart/home/selenium/BaseSeleniumTest.java), line 44 to `application.properties`
    * `cc.addConfiguration(new Configurations().properties(new File("application-docker.properties")));`

If the tests are run on docker, a video of them will be saved to the [`tests` folder](./Selenium-Tests/tests)
NOTE: you must close the `Selenium Chrome Docker` container to get the videos to run correctly

### Selenium Tests Debuting

The easiest way is to test something, is to start the tests in debuging and place a breakpoint somewhere.
When the code reaches the breakpoint, go to `http://localhost:4444` -> session -> click on the camera.
The password is `secret`.