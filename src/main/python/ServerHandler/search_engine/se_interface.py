from abc import ABC, abstractmethod
from typing import Iterable


class SearchEngine(ABC):
    """
    Common search engine interface for all implementations of search engines
    in this project.

    #### Methods:
    _verbose(mode: int) -> None:
        Private methods for handling verbose options (only use for debugging).

    search(query: str, top_k: int, verbose: int) -> list[dict]
        Return a list of dictionaries containing articles's attributes which are
        found after querying.

    """

    @abstractmethod
    def _verbose(self, mode: int) -> None:
        """
        Private methods for handling verbose options (only use for debugging).
        """
        pass

    @abstractmethod
    def search(self, query: str, top_k=5, verbose=0) -> Iterable[dict]:
        """
         Return a list of dictionaries containing articles's attributes which are
        found after querying.

        #### Parameters
        query : str
            Query string.
        top_k : int
            Number of items to returns, default 5.
        verbose : int
            Option for printing query's results onto console, default 0 for no verbose.

        #### Returns:
        List of dictionaries containing articles' attributes.
        """
        pass
