import socket
import threading
from time import sleep

HOST = '127.0.0.1'
PORT = 44441

# def send(s):
# 	while True:
# 		msg = input().encode()
# 		s.sendall(msg)

# def receive(s):
# 	while True:
# 		msg = s.recv(1024)
# 		print(msg)

# s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
# s.connect((HOST, PORT))
# threading.Thread(target=send, args=(s,)).start()
# threading.Thread(target=receive, args=(s,)).start()


def send():
	while True:
		msg = input().encode()
		s.sendto(msg, (HOST, PORT))

def receive():
	while True:
		msg, _ = s.recvfrom(1024)
		print(msg)

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.sendto(b'', (HOST, PORT))
threading.Thread(target=send).start()
threading.Thread(target=receive).start()


