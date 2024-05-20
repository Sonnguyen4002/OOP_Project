import pandas as pd
from haystack import Document
from abc import ABC

class BlockchainDB(ABC):
    def __init__(self, fileDestination, docs = []):
        self.__fileDestination = fileDestination
        self.__docs = docs

    def getFileDestination(self):
        return self.__fileDestination

    def setDocs(self, docs):
        self.__docs = docs

    def process_data(self, delimiter, engine):
        pass

    def getAllArticles(self):
        return self.__docs

    def getArticle(self, id):
        check = False
        for doc in self.__docs:
            if doc.id == id:
                check = True
                return doc
        if check is False:
            return "No article found!!"

    def addArticle(self, doc):
        if not type(doc) is Document:
            raise TypeError("Only documents are allowed")
        self.__docs.append(doc)
        return

    def deleteArticle(self, id):
        check = False
        for doc in self.__docs:
            if doc.id == id:
                check = True
                del self.__docs[self.__docs.index(doc)]
        if check is False:
            return "Article not found to be deleted!!"
        return

class ExcelDB(BlockchainDB):
    def __init__(self, fileDestination, docs = []):
        super().__init__(fileDestination, docs)

    def process_data(self, delimiter, engine):
        df = pd.read_csv(self.getFileDestination(), delimiter = delimiter, engine = engine)
        kis = list(df.keys())
        vas = [[df.iloc[i][ki] for ki in kis] for i in range(df.shape[0])]
        new_docs = [Document(content = df.iloc[i]["title"] + ". " + df.iloc[i]["content"],
                             meta = {ki:va for (ki, va) in zip(kis, vas[i])}) for i in range(df.shape[0])]
        self.setDocs(new_docs)