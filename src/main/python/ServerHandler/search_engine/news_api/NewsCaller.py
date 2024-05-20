from typing import Literal

from newsapi import NewsApiClient


class NewsCaller(NewsApiClient):
    def __init__(
        self,
        query: str,
        language="en",
        sort_by: Literal["relevancy", "popularity", "publishedAt"] = "publishedAt",
        page_size: int = 100,
    ):
        super().__init__(api_key="9264da2e99d64499b3175a5029bd6d9a")
        self._articles = self.get_everything(
            q=query,
            language=language,
            page_size=page_size,
            sort_by=sort_by,
        )["articles"]

    def get_articles(self):
        """Return all articles found by the api"""
        return self._articles

    def _get_article_type(self, source_name: str):
        """Function to get article type based on source name"""
        source = source_name.lower()
        if "twitter" in source:
            return "Tweet"
        if "facebook" in source:
            return "Facebook Post"
        if "reddit" in source:
            return "Reddit Post"
        return "News Article"

    def get_single_article_details(self, article_id: int):
        """Get an article's details from its id"""
        return {
            "id": article_id,
            "link": self._articles[article_id]["url"],
            "source": self._articles[article_id]["source"]["name"],
            "type": self._get_article_type(
                str(self._articles[article_id]["source"]["name"])
            ),
            "title": self._articles[article_id]["title"],
            "summary": (
                self._articles[article_id]["description"]
                if self._articles[article_id]["description"]
                else "N/A"
            ),
            "content": (
                self._articles[article_id]["content"]
                if self._articles[article_id]["content"]
                else "N/A"
            ),
            "publishDate": self._articles[article_id]["publishedAt"],
            "tags": "N/A",  # Tags/Hashtags are not available in News API
            "author": (
                self._articles[article_id]["author"]
                if self._articles[article_id]["author"]
                else "N/A"
            ),
            "category": self._articles[article_id].get(
                "category", "N/A"
            ),  # Use .get() method to handle KeyError
            "image": (
                self._articles[article_id]["urlToImage"]
                if self._articles[article_id]["urlToImage"]
                else "N/A"
            ),
        }
