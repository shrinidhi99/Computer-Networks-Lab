import socket

HOST = '127.0.0.1'
PORT = 44446

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:

	s.bind((HOST, PORT))
	s.listen(5)

	conn, addr = s.accept()

	with conn:

		while True:

			data = conn.recv(1024)

			if not data:
				break

			conn.sendall('24-07-2019'.encode())