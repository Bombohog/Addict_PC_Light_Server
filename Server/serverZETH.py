import os
import socket
import time
import Adafruit_DHT
from Crypto.Cipher import AES

HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)
flip = True
bs = AES.block_size
with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind(('', PORT))
    s.listen()
    conn, addr = s.accept()
    with conn:
        while True:
            try:
                humidity, temperature = Adafruit_DHT.read_retry(11, 4)
                if flip:
                    humi = str(humidity)
                    print(humi)
                    key='testtesttesttest'
                    cipher = AES.new(key,AES.MODE_ECB)
                    raw = humi + (bs - len(humi) % bs) * chr(bs - len(humi) % bs)
                    encryptedmsg = cipher.encrypt(raw)
                    print(encryptedmsg)
                    conn.send(bytes(encryptedmsg))
                    flip = False
                else:
                    key='testtesttesttest'
                    cipher = AES.new(key,AES.MODE_ECB)
                    temp = str(temperature)
                    raw = temp + (bs - len(temp) % bs) * chr(bs - len(temp) % bs)
                    encryptedmsg = cipher.encrypt(raw)
                    print(encryptedmsg)
                    conn.send(bytes(encryptedmsg))
                    flip = True
                time.sleep(1)
            except ConnectionResetError:
                conn.close()
                s.close()
                print("Works")
                os.system("python3 server.py")
