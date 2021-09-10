import socket
import time

import Adafruit_DHT

HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind(('', PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        print('Connected by', addr)

        while True:
            humidity, temperature = Adafruit_DHT.read_retry(11, 4)

            dataByteArray = str([int(temperature), int(humidity)])

            print(dataByteArray)
            conn.sendall(dataByteArray)


            time.sleep(5)
