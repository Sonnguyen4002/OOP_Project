import socket
from threading import Thread

import json
import pandas as pd

from search_engine import SearchEngine
from search_engine import SearchEngine1, SearchEngine2, SearchEngine3
from search_engine.blockchain_db import ExcelDB
from search_engine.pipeline import IndexPipeline, RetrieverPipeline
from haystack.document_stores.in_memory import InMemoryDocumentStore

# Typing
Socket = socket.socket
DataFrame = pd.DataFrame

IP = "127.0.0.1"
PORT = 8888
BUFFER_SIZE = 2
ENCODING = "utf-8"
MAX_CONNECTION = 5


# Encoding and decoding message
def encode_message(msg: str) -> bytes:
    msg_length = len(msg).to_bytes(BUFFER_SIZE)
    bytes_msg = msg_length + msg.encode("utf-8")
    return bytes_msg


def decode_message(bytes_msg: bytes) -> str:
    msg = bytes_msg.decode(ENCODING)
    return msg


def send_message(client: Socket, msg: str) -> None:
    byte_msg = encode_message(msg)
    client.send(byte_msg)


def receive_message(client: Socket) -> str:
    msg_header = client.recv(BUFFER_SIZE)
    msg_length = int.from_bytes(msg_header)
    bytes_msg = client.recv(msg_length)
    msg = decode_message(bytes_msg)
    return msg


class SocketServer(Socket):
    def __init__(self) -> None:
        # Search engine
        self._SE: SearchEngine = None
        # Search settings
        self._page_size = 10
        # Socket clients
        self._clients: list[Socket] = []
        self._nicknames: list[str] = []

    def set_search_engine(self, se):
        self._SE = se

    def start(self, ip: str, port: int, max_connection=5) -> None:
        if self._SE is None:
            raise Exception("No search engine is initialized.")
        super().__init__(socket.AF_INET, socket.SOCK_STREAM)
        self.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
        self.bind((ip, port))
        self.listen(max_connection)
        print(f"Server started at {ip} on port {port}")
        self._receive()

    def get_search_result(self, query: str) -> list[str]:
        results = self._SE.search(query, self._page_size)
        search_items = [json.dumps(res, allow_nan=False) for res in results]
        return search_items

    def _handle(self, client: Socket):
        while True:
            index = self._clients.index(client)
            nickname = self._nicknames[index]
            try:
                message = receive_message(client)
                client_input = message[message.find(":") + 1 :].strip()
                print(f"{nickname}: {client_input}")
                search_results = self.get_search_result(client_input)

                # Return search result
                send_message(client, f"-result {len(search_results)}")
                for i, item in enumerate(search_results):
                    send_message(client, f"-news {i} {item}")
                send_message(client, "-refresh")
            except Exception as e:
                print(e)
                self._clients.remove(client)
                self._nicknames.remove(nickname)
                client.close()
                break

    def _receive(self):
        while True:
            # Accepting a client connection
            client, address = self.accept()
            print(f"New connection from {address[0]}:{address[1]}")

            # Get client's nickname
            send_message(client, "-nick")
            nickname = receive_message(client)
            self._nicknames.append(nickname)
            self._clients.append(client)
            print(f"Nickname of client is {nickname}")

            # Start a seperate thread to handle the client
            thread = Thread(target=self._handle, args=(client,))
            thread.start()


if __name__ == "__main__":
    server = SocketServer()
    print(
        """Please select a search engine:
    1: SearchEngine1 - sklearn text processing with local database
    2: SearchEngine2 - newsapi with quick search time, no local database
    3: SearchEngine3 - Haystack Pipeline with local database"""
    )
    num_input = int(input("Input corresponding number: "))
    match num_input:
        case 1:
            database = pd.read_csv(
                "./Database/news_change_delimiter.csv", delimiter="::", engine="python"
            )
            server.set_search_engine(SearchEngine1(database))
        case 2:
            server.set_search_engine(SearchEngine2())
        case 3:
            my_db = ExcelDB("./Database/news_change_delimiter.csv")
            my_db.process_data(delimiter="::", engine="python")
            index = IndexPipeline(
                docEmbedderModel="sentence-transformers/all-MiniLM-L6-v2",
                docs=my_db.getAllArticles(),
                documentStore=InMemoryDocumentStore(),
            )
            index.execute()
            retrieve = RetrieverPipeline(
                textEmbedderModel="sentence-transformers/all-MiniLM-L6-v2",
                rankModel="BAAI/bge-reranker-base",
                embeddedDocumentStore=index.getDocumentStore(),
            )
            server.set_search_engine(SearchEngine3(retrievePipeline=retrieve))
    server.start(IP, PORT)
