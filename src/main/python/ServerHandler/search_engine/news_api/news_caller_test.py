import os
import csv

from NewsCaller import NewsCaller


# Main function
def main():
    query = "usd coin"
    language = "en"
    sort_by = "popularity"
    page_size = 100
    print(f"Query: {query}")
    print(f"Language: {language}")
    print(f"Sort by: {sort_by}")
    print(f"Page size: {page_size}")
    news_caller = NewsCaller(query, language, sort_by, page_size)

    file_path = "./Database/blockchain_100.csv"
    file_exists = os.path.isfile(file_path)
    fields = [
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
        writer = csv.DictWriter(file, fieldnames=fields)
        # Write the header only if the file does not exist
        if not file_exists:
            writer.writeheader()
        # Write the articles to the CSV file
        for i in range(page_size):
            article_details = news_caller.get_single_article_details(i)
            writer.writerow(article_details)

    print(f"Updated {file_path} successfully.")


if __name__ == "__main__":
    main()
