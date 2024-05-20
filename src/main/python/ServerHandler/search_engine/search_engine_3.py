from typing import Iterable, Literal
from pprint import pp as pprint

import numpy as np

from se_interface import SearchEngine
from blockchain_db import ExcelDB
from pipeline import IndexPipeline, RetrieverPipeline
from haystack.document_stores.in_memory import InMemoryDocumentStore


def fillnan(atts: dict) -> None:
    for k, v in atts.items():
        if isinstance(v, str):
            continue
        if np.isnan(v):
            print("nan")
            atts[k] = "null"


class SearchEngine3(SearchEngine):
    def __init__(self, retrievePipeline: RetrieverPipeline):
        self.__retrievePipeline = retrievePipeline

    def _verbose(self, mode: int, results: Iterable[dict]) -> None:
        if mode == 0:
            return
        for res in results:
            item_dict = {
                "id": res["id"],
                "content": res["content"][:25] + "...",
                "publishDate": res["publishDate"],
            }
            pprint(item_dict)
        print(res.keys())

    def search(self, query: str, top_k=5, verbose: Literal[0, 1] = 0):
        self.__retrievePipeline.execute()

        output = self.__retrievePipeline.run(
            {
                "text_embedder": {"text": query},
                "bm25_retriever": {"query": query},
                "ranker": {"query": query},
            }
        )
        results = []
        origin = output["ranker"]["documents"][:top_k]
        process = [doc.meta for doc in origin]
        for i in range(top_k):
            del process[i]["source_id"]
            stuff = list(process[i].items())[:-1]
            stuff.insert(0, ("id", int(origin[i].id)))
            stuff.insert(1, ("content", origin[i].content))
            new_doc_meta = dict(stuff)
            fillnan(new_doc_meta)
            results.append(new_doc_meta)
        self._verbose(verbose, results)
        return results


if __name__ == "__main__":
    print("Importing database")
    my_db = ExcelDB("./Database/news_change_delimiter.csv")
    my_db.process_data(delimiter="::", engine="python")

    print("Setting up pipeline")
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

    print("Initializing search engine")
    search_engine = SearchEngine3(retrievePipeline=retrieve)

    # Perform search
    query = "New York crypto news"
    print(f"Query: {query}")
    results = search_engine.search(query, verbose=1)
    print("Done")
