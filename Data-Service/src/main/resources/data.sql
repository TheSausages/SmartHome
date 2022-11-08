INSERT INTO HOME (id, name, street, city, post_code, country) VALUES (1, 'PWR', 'STANISŁAWA WYSZYŃSKIEGO 27', 'WROCŁAW', '50-370', 'POLSKA');

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (1, 'AIR_POLLUTION', 'Dual Scan AC3858/51', 'PHILIPS', 'HIBWCDUIYHWASDAD', '2022-11-01', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, consumed_electricity, created_at, home_id) VALUES (1, 'AIR_FILTER', 'Dual Scan AC3858/51', 'PHILIPS', 'HIBWCDUIYHWASDAD', '60', '2022-11-01', 1);

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (2, 'TEMPERATURE', 'Klimatyzator ścienny ECO KEX', 'KAISAI', 'HIBWCDUIYHWASDAE', '2022-11-05', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, consumed_electricity, created_at, home_id) VALUES (2, 'AIR_CONDITIONER', 'Klimatyzator ścienny ECO KEX', 'PHILIPS', 'HIBWCDUIYHWASDAE', '3500', '2022-11-05', 1);

INSERT INTO SENSOR (id, type, name, manufacturer, serial_number, created_at, home_id) VALUES (3, 'AIR_HUMIDITY', 'Nawilżacz powietrza HU4801/01', 'PHILIPS', 'HIBWCDUIYHWASDAF', '2022-11-05', 1);
INSERT INTO FUNCTIONAL_DEVICE (id, type, name, manufacturer, serial_number, consumed_electricity, created_at, home_id) VALUES (3, 'AIR_HUMIDIFIER', 'Nawilżacz powietrza HU4801/01', 'PHILIPS', 'HIBWCDUIYHWASDAF', '20', '2022-11-05', 1);