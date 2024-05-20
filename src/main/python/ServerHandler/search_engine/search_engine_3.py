from se_interface import SearchEngine
from typing import Iterable, Literal
from ppprint import pp as pprint

class SearchEngine3(SearchEngine):
    def __init__(self, retrievePipeline):
        self.__retrievePipeline = retrievePipeline

    def _verbose(self, mode: int, results: Iterable[dict]) -> None:
        if mode == 0:
            return
        for res in results:
            item_dict = {"id": res["id"],
                         "content": res["content"][:25] + "...",
                         "publishDate": res["publishDate"],
                        }
            pprint(item_dict)
        print(res.keys())

    def search(self, query: str, top_k: int, verbose: Literal[0, 1] = 0):
        self.__retrievePipeline.execute()

        output = self.__retrievePipeline.run(
            {"text_embedder": {"text": query}, "bm25_retriever": {"query": query}, "ranker": {"query": query}})
        results = []
        origin = output["ranker"]["documents"][:top_k]
        process = [doc.meta for doc in origin]
        for i in range(top_k):
            del process[i]["source_id"]
            stuff = list(process[i].items())
            stuff.insert(0, ("id", origin[i].id))
            new_doc_meta = dict(stuff)
            results.append(new_doc_meta)
        self._verbose(verbose, results)
        return results
