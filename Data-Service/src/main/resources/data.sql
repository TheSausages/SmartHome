INSERT INTO HOME (id, name, street, city, post_code, country, latitude, longitude) VALUES (1, 'PWR', 'STANISŁAWA WYSZYŃSKIEGO 27', 'WROCŁAW', '50-370', 'POLSKA', 51.10747999302326,17.06091170465116);

INSERT INTO USER_HOME(home_id, user_id) VALUES (1, '467a809a-d893-48c2-85e2-82f9ce4b1560');

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (1, 'AIR_POLLUTION', 'Dual Scan AC3858/51', 'PHILIPS', 'HIBWCDUIYHWASDAD', '2022-11-01', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, created_at, home_id, power_level, active) VALUES (1, 'AIR_FILTER', 'Dual Scan AC3858/51', 'PHILIPS', 'HIBWCDUIYHWASDAD', '2022-11-01', 1, 3, true);

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (2, 'TEMPERATURE', 'Klimatyzator ścienny ECO KEX', 'KAISAI', 'HIBWCDUIYHWASDAE', '2022-11-05', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, created_at, home_id, power_level, active) VALUES (2, 'AIR_CONDITIONER', 'Klimatyzator ścienny ECO KEX', 'PHILIPS', 'HIBWCDUIYHWASDAE', '2022-11-05', 1, 3, true);

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (3, 'AIR_HUMIDITY', 'Nawilżacz powietrza HU4801/01', 'PHILIPS', 'HIBWCDUIYHWASDAF', '2022-11-05', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, created_at, home_id, power_level, active) VALUES (3, 'AIR_HUMIDIFIER', 'Nawilżacz powietrza HU4801/01', 'PHILIPS', 'HIBWCDUIYHWASDAF', '2022-11-05', 1, 3, true);