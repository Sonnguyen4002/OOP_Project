import os
import csv

from newsapi import NewsApiClient

# Initialize News API client
newsapi = NewsApiClient(api_key="9264da2e99d64499b3175a5029bd6d9a")


# Function to get article details
def get_articles(query, language="en", page_size=100, sort_by="publishedAt"):
    articles = newsapi.get_everything(
        q=query, language=language, page_size=page_size, sort_by=sort_by
    )
    return articles["articles"]


# Function to get article type based on source name
def get_article_type(source_name):
    if "twitter" in source_name.lower():
        return "Tweet"
    elif "facebook" in source_name.lower():
        return "Facebook Post"
    else:
        return "News Article"


# Function to gather article details with an ID into a dictionary
def get_article_details(article, article_id):
    return {
        "id": article_id,
        "link": article["url"],
        "source": article["source"]["name"],
        "type": get_article_type(article["source"]["name"]),
        "title": article["title"],
        "summary": article["description"] if article["description"] else "N/A",
        "content": article["content"] if article["content"] else "N/A",
        "publishDate": article["publishedAt"],
        "tags": "N/A",  # Tags/Hashtags are not available in News API
        "author": article["author"] if article["author"] else "N/A",
        "category": article.get(
            "category", "N/A"
        ),  # Use .get() method to handle KeyError
        "image": article["urlToImage"] if article["urlToImage"] else "N/A",
    }


# Main function
def main():
    query = "usd coin"
    language = "en"
    sort_by = "popularity"  # Can be 'relevancy', 'popularity', or 'publishedAt'
    articles = get_articles(query, language, sort_by=sort_by)

    file_path = "./Database/top100_blockchain.csv"
    file_exists = os.path.isfile(file_path)
    fieldnames = [
        "id",
        "link",
        "source",
        "type",
        "title",
        "summary",
        "content",
        "publishDate",
        "tags",
        "author",
        "category",
        "image",
    ]

    # Open the CSV file in append mode
    with open(file_path, mode="w", newline="", encoding="utf-8") as file:
        writer = csv.DictWriter(file, fieldnames=fieldnames)

        # Write the header only if the file does not exist
        if not file_exists:
            writer.writeheader()

        # Get the current max ID in the file if it exists
        current_max_id = 0
        if file_exists:
            with open("top100_blockchain.csv", mode="r", newline="") as read_file:
                reader = csv.DictReader(read_file)
                for row in reader:
                    current_max_id = max(current_max_id, int(row["id"]))

        # Write the articles to the CSV file
        for i, article in enumerate(articles):
            article_details = get_article_details(article, current_max_id + i)
            print(i, current_max_id)
            print(article)
            print(articles[i]["source"]["name"])

            writer.writerow(article_details)

    print("CSV file 'articles.csv' updated successfully.")


if __name__ == "__main__":
    main()
