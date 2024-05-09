import socket
from threading import Thread


IP = "127.0.0.1"
PORT = 8888
HEADER_LENGTH = 2
ENCODING = "utf-8"
MAX_CONNECTION = 5

# Start the server
server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
server.bind((IP, PORT))
server.listen(MAX_CONNECTION)
print(f"Server started at {IP} on port {PORT}")

clients: list[socket.socket] = []
nicknames: list[str] = []


def encode_message(msg: str) -> bytes:
    msg_length = len(msg).to_bytes(HEADER_LENGTH)
    bytes_msg = msg_length + msg.encode("utf-8")
    return bytes_msg


def decode_message(bytes_msg: bytes):
    msg = bytes_msg.decode(ENCODING)
    return msg


def send_message(client: socket.socket, msg: str) -> None:
    byte_msg = encode_message(msg)
    client.send(byte_msg)


def broadcast(message: str) -> None:
    for client in clients:
        send_message(client, message)


def receive_message(client: socket.socket) -> str:
    msg_header = client.recv(HEADER_LENGTH)
    msg_length = int.from_bytes(msg_header)
    bytes_msg = client.recv(msg_length)
    msg = decode_message(bytes_msg)
    return msg


def handle(client: socket.socket):
    while True:
        try:
            message = receive_message(client)

            client_input = message[message.find(":") + 1 :].strip()
            print(client_input)
            if client_input[0] == "-":
                # Switch to command mode
                cmd, *args = client_input.split(" ")
                cmd = cmd.lower()
                if cmd == "-upper":
                    send_message(client, " ".join(args).upper())
                elif cmd == "-lower":
                    send_message(client, " ".join(args).lower())
            else:
                broadcast(message)
        except Exception as e:
            print(e)
            index = clients.index(client)
            clients.remove(client)
            client.close()
            nickname = nicknames[index]
            broadcast(f"{nickname} has left the chat")
            nicknames.remove(nickname)
            break


def receive():
    while True:
        client, address = server.accept()
        print(f"New connection from {address[0]}:{address[1]}")

        send_message(client, "-nick")
        nickname = receive_message(client)

        nicknames.append(nickname)
        clients.append(client)

        print(f"Nickname of client is {nickname}")
        broadcast(f"{nickname} joined the chat!")
        send_message(client, "Connected to the server!")

        thread = Thread(target=handle, args=(client,))
        thread.start()


receive()
