import socket

HOST = '127.0.0.1'
PORT = 44446

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect((HOST, PORT))
s.sendall('What is the date today?'.encode())
msg = s.recv(1024)
