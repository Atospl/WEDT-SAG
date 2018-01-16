from gensim.models import Doc2Vec
from utils import Article
import pickle


class Article2Vec:

    def __init__(self, model_file):
        self.model = Doc2Vec.load(model_file)

    def vector(self, article: Article):
        vector = self.model.infer_vector(article.words())
        return vector.tolist()

