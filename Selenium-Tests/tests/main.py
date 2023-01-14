import requests
import json
import time
import random
import csv

url_temperature = "http://localhost:8081/api/data/temperature"
url_humidity = "http://localhost:8081/api/data/humidity"
url_air_quality = "http://localhost:8081/api/data/air-quality"
url_actuator = "http://localhost:8081/actuator/all"

headers_temperature = {
    'Serial-Number': 'HIBWCDUIYHWASDAE',
    'Content-Type': 'application/json',
}

headers_humidity = {
    'Serial-Number': 'HIBWCDUIYHWASDAF',
    'Content-Type': 'application/json',
}

headers_air_quality = {
    'Serial-Number': 'HIBWCDUIYHWASDAF',
    'Content-Type': 'application/json',
}


def sent_requests(n):
    i = 0
    for k in range(1, n + 1):
        payload_temperature = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAE",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "TEMPERATURE",
            "temperature": random.randrange(18, 26)
        })

        payload_humidity = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAF",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "AIR_HUMIDITY",
            "humidity": random.randrange(40, 60)
        })

        payload_air_quality = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAD",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "AIR_POLLUTION",
            "pm25": random.randrange(1, 20),
            "iai": random.randrange(1, 6),
            "gas": random.randrange(1, 4)
        })

        requests.request("POST", url_temperature, headers=headers_temperature, data=payload_temperature)
        i += 1
        requests.request("POST", url_humidity, headers=headers_humidity, data=payload_humidity)
        i += 1
        requests.request("POST", url_air_quality, headers=headers_air_quality, data=payload_air_quality)
        i += 1

    print(i, "requests have been sent.")


def collect_metrics(j):
    f = open('results.csv', 'a', newline='')
    writer = csv.writer(f)
    response = requests.request("GET", url_actuator, headers={}, data={})
    parsed_metrics = json.loads(response.text)

    header = ["number_of_requests"]
    if j == 1:
        for metric in parsed_metrics:
            header.append(metric["name"])
        writer.writerow(header)

    data_row = [str(j*3)]
    for metric in parsed_metrics:
        data_row.append(metric["measurements"][0]["value"])
    writer.writerow(data_row)

    f.close()
    print("Saved data to csv for", j*3, "requests.")


if __name__ == '__main__':
    f = open('results.csv', 'w')
    f.close()

    for j in range(1, 11):
        sent_requests(j)
        time.sleep(1)
        collect_metrics(j)
        time.sleep(5)
