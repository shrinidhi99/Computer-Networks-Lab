import socket

HOST = '127.0.0.1'
PORT = 44448

# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
# 	s.connect((HOST, PORT))
# 	for i in range(4):

# 		data = s.recv(1024)
# 		print('received msg: ', data)
# 		s.sendall('message {} from client'.format(i).encode())

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
	s.sendto(b'', (HOST, PORT))
	for i in range(4):

		data, _ = s.recvfrom(1024)
		print('received msg:', data)
		s.sendto('message {} from client'.format(i).encode(), (HOST, PORT))