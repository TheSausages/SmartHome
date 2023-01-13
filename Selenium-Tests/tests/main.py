import requests
import json
import time
import random

url1 = "http://localhost:8081/api/data/temperature"
url2 = "http://localhost:8081/api/data/humidity"
url3 = "http://localhost:8081/api/data/air-quality"

headers1 = {
    'Serial-Number': 'HIBWCDUIYHWASDAE',
    'Content-Type': 'application/json',
}

headers2 = {
    'Serial-Number': 'HIBWCDUIYHWASDAF',
    'Content-Type': 'application/json',
}

headers3 = {
    'Serial-Number': 'HIBWCDUIYHWASDAF',
    'Content-Type': 'application/json',
}

while True:
    for i in range(33):
        payload1 = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAE",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "TEMPERATURE",
            "temperature": random.randrange(18, 26)
        })

        payload2 = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAF",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "AIR_HUMIDITY",
            "humidity": random.randrange(40, 60)
        })

        payload3 = json.dumps({
            "serialNumber": "HIBWCDUIYHWASDAD",
            "timestamp": "2023-01-13 21:51:28.796",
            "type": "AIR_POLLUTION",
            "pm25": random.randrange(1, 20),
            "iai": random.randrange(1, 6),
            "gas": random.randrange(1, 4)
        })

        requests.request("POST", url1, headers=headers1, data=payload1)
        requests.request("POST", url2, headers=headers2, data=payload2)
        requests.request("POST", url3, headers=headers3, data=payload3)

    print("Sent 99 requests")
    time.sleep(10)
