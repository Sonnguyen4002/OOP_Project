from haystack.utils import Secret
import pandas as pd
from haystack import Document
from haystack.document_stores.in_memory import InMemoryDocumentStore
from haystack.telemetry import tutorial_running
tutorial_running(33)

# Where the "data1.csv" is located.
import os
# os.chdir(r'C:\Users\Lenovo\Downloads')

from haystack.components.writers import DocumentWriter
from haystack.components.embedders import SentenceTransformersDocumentEmbedder
from haystack.components.preprocessors import DocumentSplitter
from haystack import Pipeline
from haystack.utils import ComponentDevice

from haystack.components.retrievers.in_memory import InMemoryBM25Retriever, InMemoryEmbeddingRetriever
from haystack.components.embedders import SentenceTransformersTextEmbedder

from haystack.components.joiners import DocumentJoiner

from haystack.components.rankers import TransformersSimilarityRanker

# Initialize the DocumentStore

document_store = InMemoryDocumentStore()

# Fetching and processing Documents. For searching, we use (title + content)
df = pd.read_csv("/Users/nguyentrithanh/Downloads/OOP_Project-feature-client/ServerHandler/ngu.csv")
docs = []
for i in range(df.shape[0]):
    if i < 20:
        docs.append(Document(content = df.iloc[i]["title"] + ". " + df.iloc[i]["content"], meta={"title": df.iloc[i]["title"], "summary": df.iloc[i]["summary"], "publishDate": df.iloc[i]["publishDate"].split('T')[0], "tags": df.iloc[i]["tags"], "category": df.iloc[i]["category"]}))
    else: break

# Indexing Documents with a Pipeline
document_splitter = DocumentSplitter(split_by = "word", split_length = 30, split_overlap= 5)

# document_embedder = SentenceTransformersDocumentEmbedder(model = "sentence-transformers/all-MiniLM-L6-v2", device = ComponentDevice.from_str("cuda: 0"), batch_size = 5, token = Secret.from_token("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"))
document_embedder = SentenceTransformersDocumentEmbedder(model = "sentence-transformers/all-MiniLM-L6-v2", device = ComponentDevice.from_str("cpu"), batch_size = 5, token = Secret.from_token("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"))



document_writer = DocumentWriter(document_store)

indexing_pipeline = Pipeline()
indexing_pipeline.add_component("document_splitter", document_splitter)
indexing_pipeline.add_component("document_embedder", document_embedder)
indexing_pipeline.add_component("document_writer", document_writer)

indexing_pipeline.connect("document_splitter", "document_embedder")
indexing_pipeline.connect("document_embedder", "document_writer")
indexing_pipeline.run({"document_splitter": {"documents": docs}})

# Initialize Retrievers and the Embedder
# text_embedder = SentenceTransformersTextEmbedder(model = "sentence-transformers/all-MiniLM-L6-v2", device = ComponentDevice.from_str("cuda: 0"), batch_size = 1)
text_embedder = SentenceTransformersTextEmbedder(model = "sentence-transformers/all-MiniLM-L6-v2", device = ComponentDevice.from_str("cpu"), batch_size = 1)



embedding_retriever = InMemoryEmbeddingRetriever(document_store)

bm25_retriever = InMemoryBM25Retriever(document_store)

# Join Retrieval Results

document_joiner = DocumentJoiner()

# Rank the result

ranker = TransformersSimilarityRanker(model = "BAAI/bge-reranker-base", top_k = 15)

# Create the hybrid retrieval pipeline
hybrid_retriever = Pipeline()
hybrid_retriever.add_component("text_embedder", text_embedder)
hybrid_retriever.add_component("embedding_retriever", embedding_retriever)
hybrid_retriever.add_component("bm25_retriever", bm25_retriever)
hybrid_retriever.add_component("document_joiner", document_joiner)
hybrid_retriever.add_component("ranker", ranker)

hybrid_retriever.connect("text_embedder","embedding_retriever")
hybrid_retriever.connect("embedding_retriever", "document_joiner")
hybrid_retriever.connect("bm25_retriever", "document_joiner")
hybrid_retriever.connect("document_joiner","ranker")

# Visualize the pipeline
hybrid_retriever.draw("new_pipeline.png")

# Query input
query = "Bitcoin May Rally to $80K on Triangle Break: Technical Analysis"

# Running the search engine
result = hybrid_retriever.run(
    {"text_embedder": {"text": query}, "bm25_retriever": {"query": query}, "ranker": {"query": query}}
)


# def filter(result):
#     filtered_result = dict()
#     filtered_title = []

#     for doc in result["documents"]:
#         if doc.meta["title"] in filtered_title:
#             continue
#         else:
#             filtered_title.append(doc.meta['title'])
#             filtered_result

# How the result is printed out.
def pretty_print_results(prediction):
    count = 1
    filtered_title = []
    for doc in prediction["documents"]:
        if doc.meta["title"] in filtered_title:
            continue
        else:
            print(doc.meta["title"], "\t", doc.score)
            print(doc.meta["summary"])
            print(count)
            print("\n", "\n")
            filtered_title.append(doc.meta["title"])
            count += 1

        if count == 10:
            break
        
pretty_print_results(result["ranker"])

# print(result["ranker"])