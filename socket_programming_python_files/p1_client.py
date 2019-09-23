import socket

HOST = '127.0.0.1'
PORT = 44445

# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:

# 	s.connect((HOST, PORT))

# 	msg = s.recv(1024)

# print('Received message from server:', msg)


with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:

	s.sendto(b'', (HOST, PORT))

	data, addr = s.recvfrom(1024)

print('Received message from server:', data)