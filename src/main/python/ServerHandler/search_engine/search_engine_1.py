from typing import Iterable, Literal
from pprint import pp as pprint


import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sentence_transformers import SentenceTransformer
from sklearn.metrics.pairwise import cosine_similarity

from se_interface import SearchEngine

DataFrame = pd.DataFrame


class SearchEngine1(SearchEngine):
    """Search engine that uses sklearn library text processing."""

    def __init__(self, database: str | DataFrame):
        self._data = self._import_database(database)
        self._model = SentenceTransformer("paraphrase-MiniLM-L6-v2")
        self._tfidf_vectorizer = TfidfVectorizer()
        self._tfidf_matrix = self._calculate_tfidf_matrix()

    def _import_database(self, database: str | DataFrame) -> DataFrame:
        if isinstance(database, str):
            print(f"Loading data base from {database}")
            return pd.read_csv("./Database/database.csv")
        if isinstance(database, DataFrame):
            print("Loading data base from DataFrame")
            return database

    def _calculate_tfidf_matrix(self):
        tfidf_matrix = self._tfidf_vectorizer.fit_transform(
            self._data["content"].values
        )
        return tfidf_matrix

    def _verbose(
        self,
        mode: int,
        indices: Iterable[int],
        combined_similarity: Iterable[float],
        results: Iterable[dict],
    ) -> None:
        if mode == 0:
            return
        for idx, res in zip(indices, results):
            item_dict = {
                "id": idx,
                "content": res["content"][:25] + "...",
                "publishDate": res["publishDate"],
            }
            if mode == 2:
                item_dict.update({"similarity": combined_similarity[idx]})
            pprint(item_dict)

    def search(self, query: str, top_k=5, verbose: Literal[0, 1, 2] = 0) -> list[dict]:
        query_embedding = self._model.encode([query])[0]
        query_tfidf = self._tfidf_vectorizer.transform([query])
        tfidf_similarity = cosine_similarity(query_tfidf, self._tfidf_matrix)[0]
        semantic_similarity = cosine_similarity(
            [query_embedding], self._model.encode(self._data["content"].values)
        )[0]
        combined_similarity = 0.5 * tfidf_similarity + 0.5 * semantic_similarity
        indices = np.argsort(combined_similarity)[-top_k:][::-1]
        results = [self._data.iloc[idx].fillna("null").to_dict() for idx in indices]
        self._verbose(verbose, indices, combined_similarity, results)
        return results


if __name__ == "__main__":
    # Create search engine instance
    print("Initializing search engine")
    data_path = "./Database/database.csv"

    # Load data from CSV file
    print(f"Loading data from {data_path}")
    database = pd.read_csv("./Database/database.csv")
    search_engine = SearchEngine1(database)

    # Perform search
    query = "New York crypto news"
    print(f"Query: {query}")
    results = search_engine.search(query, verbose=1)
    print("Done")
