DROP TABLE IF EXISTS HOME CASCADE;
CREATE TABLE HOME (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    street VARCHAR(128) NOT NULL,
    city VARCHAR(128) NOT NULL,
    post_code VARCHAR(128) NOT NULL,
    country VARCHAR(128) NOT NULL,
    latitude FLOAT NOT NULL,
    longitude FLOAT NOT NULL,
    preferred_temp INT NOT NULL DEFAULT 21,
    preferred_hum INT NOT NULL DEFAULT 45,
    hours VARCHAR(128) NOT NULL DEFAULT ''
);

DROP TABLE IF EXISTS USER_HOME CASCADE;
CREATE TABLE USER_HOME (
    home_id SERIAL,
    user_id uuid,
    FOREIGN KEY (home_id) REFERENCES HOME(ID)
);

DROP TYPE IF EXISTS SENSOR_TYPE CASCADE;
CREATE TYPE SENSOR_TYPE AS ENUM ('TEMPERATURE', 'AIR_POLLUTION', 'AIR_HUMIDITY');
DROP TABLE IF EXISTS SENSOR CASCADE;
CREATE TABLE SENSOR (
    id   SERIAL PRIMARY KEY,
    type varchar(128) NOT NULL,
    name VARCHAR(128) NOT NULL,
    manufacturer VARCHAR(128) NOT NULL,
    serial_number VARCHAR(128) NOT NULL,
    created_at DATE,
    is_connected bool NOT NULL DEFAULT TRUE,
    home_id INT,
    FOREIGN KEY (home_id) REFERENCES HOME(ID)
);

DROP TYPE IF EXISTS MEASUREMENT_TYPE CASCADE;
CREATE TYPE MEASUREMENT_TYPE AS ENUM ('CELSIUS', 'HUMIDITY', 'IAI', 'PM25', 'GAS');
DROP TABLE IF EXISTS MEASUREMENT CASCADE;
CREATE TABLE MEASUREMENT (
    id   SERIAL PRIMARY KEY,
    type varchar(128) NOT NULL,
    value float NOT NULL,
    created_at TIMESTAMP,
    sensor_id INT,
    FOREIGN KEY (sensor_id) REFERENCES SENSOR(ID)
);

DROP TYPE IF EXISTS DEVICE_TYPE CASCADE;
CREATE TYPE DEVICE_TYPE AS ENUM ('AIR_FILTER', 'AIR_CONDITIONER', 'AIR_HUMIDIFIER');
DROP TABLE IF EXISTS FUNCTIONAL_DEVICE CASCADE;
CREATE TABLE FUNCTIONAL_DEVICE (
    id   SERIAL PRIMARY KEY,
    type varchar(128) NOT NULL,
    name VARCHAR(128) NOT NULL,
    manufacturer VARCHAR(128) NOT NULL,
    serial_number VARCHAR(128) NOT NULL,
    created_at DATE,
    is_connected bool NOT NULL DEFAULT TRUE,
    home_id INT,
    power_level INT,
    FOREIGN KEY (home_id) REFERENCES HOME(ID)
);