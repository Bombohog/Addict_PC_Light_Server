import json
import socket

sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print("Socket created ...")

port = 8001
sock.bind(('', port))
sock.listen(5)


print('socket is listening')

jsonResult = {"first": "You're", "second": "Awsome!"}


while True:
    c, addr = sock.accept()
    print('got connection from ', addr)

    sock.send(bytes(json.dumps(jsonResult), "utf-8"))
