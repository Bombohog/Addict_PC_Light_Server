import os
import socket
import time
import Adafruit_DHT
HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)
flip = True
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind(('', PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        while True:
            humidity, temperature = Adafruit_DHT.read_retry(11, 4)
            if flip:
                n = len(str(humidity))
                nString = str(n).encode()
                conn.send(bytes(nString))
                conn.send(bytes(str(humidity).encode()))
                flip = False
            else:
                n = len(str(temperature))
                conn.send(bytes(str(n).encode()))
                conn.send(bytes(str(temperature).encode()))
                flip = True
            time.sleep(1)
