import socket

HOST = '10.100.14.150'
PORT = 2200

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.connect((HOST, PORT))
    msg = s.recv(1024)
    print("received msg" , msg)

    s.sendall(b'hi from client')