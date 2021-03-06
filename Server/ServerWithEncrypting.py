from Crypto.Cipher import AES
import base64
import socket
import Adafruit_DHT

block_size = AES.block_size
HOST = '127.0.0.1'  # Standard loopback interface address (localhost)
PORT = 8001  # Port to listen on (non-privileged ports are > 1023)
tal = 1

#unpad = lambda s : s[0:-ord(s[-1])]
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
        try:
            while True:
                data = conn.recv(1024)
                humidity, temperature = Adafruit_DHT.read_retry(11, 4)
                key='sixteencharacter'
                plain=str(temperature) + ',' + str(humidity)
                tal = tal + 1
                plain = pad(plain)
                iv = 'jvHJ1XFt0IXBrxxx'
                cipher = AES.new(key, AES.MODE_CBC, iv)
                encrypted_bytes = cipher.encrypt(plain)
                encrypted_text = base64.urlsafe_b64encode(encrypted_bytes).decode("utf-8")
                print('Encrypted Text: ' + encrypted_text)
                conn.send(encrypted_text.encode())
        except ConnectionResetError:
            conn.close()
            s.close()
            print("Works")

