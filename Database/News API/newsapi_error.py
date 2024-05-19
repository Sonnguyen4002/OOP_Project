from newsapi import NewsApiClient
import csv
import os

# Create a NewsClient class to handle API interactions.
class NewsClient:
    def __init__(self, api_key):
        self.newsapi = NewsApiClient(api_key=api_key)

    def get_articles(self, query, language='en', page_size=100, sort_by='publishedAt'):
        articles = self.newsapi.get_everything(q=query,
                                               language=language,
                                               page_size=page_size,
                                               sort_by=sort_by)
        return articles['articles']

# Create an Article class to represent article details.
class Article:
    def __init__(self, article_data, article_id):
        self.id = article_id
        self.link = article_data['url']
        self.source = article_data['source']['name']
        self.type = self.get_article_type(self.source)
        self.title = article_data['title']
        self.summary = article_data['description'] if article_data['description'] else "N/A"
        self.content = article_data['content'] if article_data['content'] else "N/A"
        self.publish_date = article_data['publishedAt']
        self.tags = "N/A"
        self.author = article_data['author'] if article_data['author'] else "N/A"
        self.category = article_data.get('category', "N/A")
        self.image = article_data['urlToImage'] if article_data['urlToImage'] else "N/A"

    @staticmethod
    def get_article_type(source_name):
        if 'twitter' in source_name.lower():
            return 'Tweet'
        elif 'facebook' in source_name.lower():
            return 'Facebook Post'
        else:
            return 'News Article'

    def to_dict(self):
        return {
            "id": self.id,
            "link": self.link,
            "source": self.source,
            "type": self.type,
            "title": self.title,
            "summary": self.summary,
            "content": self.content,
            "publishDate": self.publish_date,
            "tags": self.tags,
            "author": self.author,
            "category": self.category,
            "image": self.image
        }

# Create a CSVHandler class to manage CSV file operations.
class CSVHandler:
    def __init__(self, filename):
        self.filename = filename

    def file_exists(self):
        return os.path.isfile(self.filename)

    def get_current_max_id(self):
        current_max_id = 0
        if self.file_exists():
            with open(self.filename, mode='r', newline='') as file:
                reader = csv.DictReader(file)
                for row in reader:
                    current_max_id = max(current_max_id, int(row['id']))
        return current_max_id

    def write_articles(self, articles):
        file_exists = self.file_exists()

        with open(self.filename, mode='w', newline='') as file:
            writer = csv.DictWriter(file, fieldnames=[
                "id", "link", "source", "type", "title", "summary", "content", "publishDate", "tags", "author", "category", "image"
            ])

            if not file_exists:
                writer.writeheader()

            for article in articles:
                writer.writerow(article.to_dict())

# Use classes in a main function to orchestrate the workflow.
def main():
    api_key = '9264da2e99d64499b3175a5029bd6d9a'
    query = 'blockchain'
    language = 'en'
    sort_by = 'popularity'

    news_client = NewsClient(api_key)
    articles_data = news_client.get_articles(query, language, sort_by=sort_by)

    csv_handler = CSVHandler('top100_blockchain.csv')
    current_max_id = csv_handler.get_current_max_id()

    articles = [Article(article_data, current_max_id + i) for i, article_data in enumerate(articles_data, start=1)]

    csv_handler.write_articles(articles)

    print("CSV file 'top100_blockchain.csv' updated successfully.")

if __name__ == "__main__":
    main()
