from se_interface import SearchEngine
from news_api import NewsCaller

from typing import Iterable, Literal
from pprint import pp as pprint


class SearchEngine2(SearchEngine):
    """Search engine that uses newsapi library for quick search time."""

    def __init__(
        self,
        language="en",
        sort_by: Literal["relevancy", "popularity", "publishedAt"] = "publishedAt",
    ):
        self.language = language
        self.sort_by = sort_by

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

    def search(self, query: str, top_k=10, verbose: Literal[0, 1] = 0):
        caller = NewsCaller(query, self.language, self.sort_by, top_k)
        print(len(caller.get_articles()))
        results = [caller.get_single_article_details(idx) for idx in range(top_k)]
        self._verbose(verbose, results)
        return results


if __name__ == "__main__":
    # Create search engine instance
    print("Initializing search engine")
    search_engine = SearchEngine2()

    # Perform search
    query = "bitcoin"
    print(f"Query: {query}")
    results = search_engine.search(query, verbose=1)
    print("Done")
