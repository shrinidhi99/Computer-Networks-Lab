import socket

HOST = '127.0.0.1'
PORT = 44445

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
    s.bind((HOST, PORT))
    _, addr = s.recvfrom(1024)
    s.sendto(b'Greeting from server ', addr)
