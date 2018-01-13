from nltk.corpus import reuters
from gensim.models.doc2vec import TaggedDocument
from gensim.models import Doc2Vec
from functools import reduce
from textwrap import wrap

from numpy.core.multiarray import dot
from gensim.matutils import unitvec


def get_tagged_documents_reuters(corpus_type):
    docs = []
    for corpus_file in filter(lambda f: f.startswith(corpus_type), reuters.fileids()):
        docs.append(TaggedDocument(words=[word for word in reuters.words(corpus_file)], tags=[corpus_file]))
    return docs


def print_tagged_document(doc):
    tags_str = reduce(lambda x, y: x + " " + str(y), doc.tags, "")
    print("TAGS:" + tags_str)
    words_str = reduce(lambda x, y: x + str(y) + " ", doc.words, "")
    for line in wrap(words_str, 80):
        print(line)


def vector_similarity(v1, v2):
    return dot(unitvec(v1), unitvec(v2))




class Article:
    def __init__(self, uid, categories, words, vector=None):
        self.id = uid
        self.categories = categories
        self.words = words
        self.vector = vector

    def __str__(self):
        ret_str = "ID: " + self.id

        #ret_str += "\nVECTOR: " + str(self.vector)

        ret_str += "\nTAGS:" + reduce(lambda x, y: x + " " + str(y), self.categories, "")

        words_str = reduce(lambda x, y: x + str(y) + " ", self.words, "")

        ret_str += reduce(lambda x, y: x + "\n" + y, wrap(words_str, 80), "")

        return ret_str


def articles_from_reuters(corpus_type):
    articles = []
    for file in filter(lambda x: x.startswith(corpus_type), reuters.fileids()):
        words = [word for word in reuters.words(file)]
        categories = [cat for cat in reuters.categories(file)]
        articles.append(Article(file, categories, words))

    return articles


def find_n_most_similar_articles(model:Doc2Vec, article_list, article: Article, n : int):

    article.vector = model.infer_vector(article.words)

    similarities = []

    for article_cmp in article_list:
        if article_cmp.vector is None:
            article_cmp.vector = model.infer_vector(article_cmp.words)

        similarity = vector_similarity(article.vector, article_cmp.vector)
        similarities.append((similarity, article_cmp))

    similarities = sorted(similarities, key=lambda k: k[0], reverse=True)

    return [s for s in similarities[0:n]]
