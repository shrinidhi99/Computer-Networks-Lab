import socket

HOST = '127.0.0.1'
PORT = 44448

# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
# 	s.bind((HOST, PORT))
# 	s.listen(5)
# 	conn, addr = s.accept()

# 	with conn:
# 		for i in range(4):
# 			conn.sendall('message {} from server'.format(i).encode())

# 			data = conn.recv(1024)

# 			print('received msg: ', data)

with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as s:
	s.bind((HOST, PORT))
	_, addr = s.recvfrom(1024)

	for i in range(4):
		s.sendto('message {} from server'.format(i).encode(), addr)
		data, _ = s.recvfrom(1024)
		print('received msg:', data)

