from nltk import RegexpTokenizer
from nltk.corpus import reuters
from gensim.models.doc2vec import TaggedDocument
from gensim.models import Doc2Vec
from functools import reduce
from textwrap import wrap

from numpy.core.multiarray import dot
from gensim.matutils import unitvec

from nltk.tokenize.moses import MosesTokenizer


def preprocess(in_string):
    tokenizer = RegexpTokenizer(r'\w+')
    return " ".join(tokenizer.tokenize(in_string))


def get_tagged_documents_reuters(corpus_type):
    docs = []
    for corpus_file in filter(lambda f: f.startswith(corpus_type), reuters.fileids()):
        words = (preprocess(reduce(lambda x, y: x + str(y) + " ",[word for word in reuters.words(corpus_file)]))).split(" ")
        docs.append(TaggedDocument(words=words, tags=[corpus_file]))
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
    def __init__(self, text, uid="", categories=None, vector=None):
        self.id = uid
        self.categories = categories
        self.text = preprocess(text)
        self.vector = vector

    def __str__(self):
        ret_str = "ID: " + self.id

        #ret_str += "\nVECTOR: " + str(self.vector)

        ret_str += "\nTAGS:" + reduce(lambda x, y: x + " " + str(y), self.categories, "")

        ret_str += reduce(lambda x, y: x + "\n" + y, wrap(self.text, 80), "")

        return ret_str

    def words(self):
        return self.text.split(" ")


def articles_from_reuters(corpus_type):
    articles = []
    for file in filter(lambda x: x.startswith(corpus_type), reuters.fileids()):
        text = reduce(lambda x, y: x + str(y) + " ", [word for word in reuters.words(file)])
        categories = [cat for cat in reuters.categories(file)]
        articles.append(Article(text, file, categories))

    return articles


def find_n_most_similar_articles(model: Doc2Vec, article_list, article: Article, n: int):

    article.vector = model.infer_vector(article.words())

    similarities = []

    for article_cmp in article_list:
        if article_cmp.vector is None:
            article_cmp.vector = model.infer_vector(article_cmp.words())

        similarity = vector_similarity(article.vector, article_cmp.vector)
        similarities.append((similarity, article_cmp))

    similarities = sorted(similarities, key=lambda k: k[0], reverse=True)

    return similarities[0:n]


def find_n_most_similar_vectors(vectors_dict: dict, query_vector_id:str, n:int = 0 ):
    query_vector = vectors_dict[query_vector_id]
    if n == 0:
        n = len(query_vector)
    similar = sorted(
        map(lambda item: (item[0], vector_similarity(query_vector, item[1])),
            vectors_dict.items()
            ),
        key=lambda k: k[1],
        reverse=True
    )

    return similar[:n]


def find_n_most_similar_pickled_vectors(vectors_dict: dict, query_vector_id:str, n:int = 3):
    for id in vectors_dict:
        vectors_dict[id] = pickle.loads(vectors_dict[id])
        find_n_most_similar_vectors(vectors_dict, query_vector_id, n)
