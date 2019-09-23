import socket
import threading

HOST = '127.0.0.1'
PORT = 44441

clients = []

# def check():
# 	while True:
# 		conn, addr = s.accept()
# 		print('connected')
# 		if conn not in clients:
# 			clients.append(conn)
# 		threading.Thread(target = receive, args = (conn, )).start()

# def transmit(msg):
# 	for conn in clients:
# 		conn.sendall(msg)

# def receive(conn):
# 	while True:
# 		msg = conn.recv(1024)
# 		if msg:
# 			transmit(msg)


# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
# 	s.bind((HOST, PORT))
# 	s.listen(5)
# 	check()


def check():
	while True:
		data, addr = s.recvfrom(1024)  # can get stuck here - confusion point
		if not data:
			print('connected', addr)
			if addr not in clients:
				clients.append(addr)
			threading.Thread(target = receive, args = (addr, )).start()
		else:
			transmit(data)

def transmit(data):
	for addr in clients:
		s.sendto(data, addr)

def receive(addr):
	while True:
		data, addr = s.recvfrom(1024) # can get stuck here - confusion point
		if data:
			transmit(data)
		else:
			print('connected', addr)
			if addr not in clients:
				clients.append(addr)
			threading.Thread(target = receive, args = (addr, )).start()


s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.bind((HOST, PORT))
check()