import os
import socket
import time
import base64
import Adafruit_DHT
from Crypto.Cipher import AES

HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)
flip = True
block_size = AES.block_size
def pad(plain_text):
    """
    func to pad cleartext to be multiples of 8-byte blocks.
    If you want to encrypt a text message that is not multiples of 8-byte blocks,
    the text message must be padded with additional bytes to make the text message to be multiples of 8-byte blocks.
    """
    number_of_bytes_to_pad = block_size - len(plain_text) % block_size
    ascii_string = chr(number_of_bytes_to_pad)
    padding_str = number_of_bytes_to_pad * ascii_string
    padded_plain_text =  plain_text + padding_str
    return padded_plain_text
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

                    iv = 'jvHJ1XFt0IXBrxxx'
                    key='sixteencharacter'
                    cipher = AES.new(key, AES.MODE_CBC, iv)
                    encryptedmsg = base64.urlsafe_b64encode(cipher.encrypt(pad(humi))).decode("utf-8")

                    print(encryptedmsg)
                    encryptedmsgencoded = str(encryptedmsg.encode())
                    n1 = len(encryptedmsgencoded)
                    n1String = str(n1).encode()

                    n2 = len(str(n1String))
                    n2String = str(n2).encode()
                    conn.send(bytes(n2String))

                    conn.send(bytes(n1String))

                    conn.send(bytes(encryptedmsgencoded))
                    flip = False
                else:

                    temp = str(temperature)

                    key='sixteencharacter'
                    iv = 'jvHJ1XFt0IXBrxxx'
                    cipher = AES.new(key, AES.MODE_CBC, iv)
                    encryptedmsg = base64.urlsafe_b64encode(cipher.encrypt(pad(temp))).decode("utf-8")
                    print(encryptedmsg)

                    encryptedmsgencoded = str(encryptedmsg.encode())
                    n1 = len(encryptedmsgencoded)
                    n1String = str(n1).encode()

                    n2 = len(str(n1String))
                    n2String = str(n2).encode()
                    conn.send(bytes(n2String))

                    conn.send(bytes(n1String))

                    conn.send(bytes(encryptedmsgencoded))
                    flip = True
                time.sleep(1)
            except ConnectionResetError:
                conn.close()
                s.close()
                print("Works")
                os.system("python3 server.py")
