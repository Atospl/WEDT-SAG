from gensim.models import Doc2Vec
from gensim.models.doc2vec import TaggedDocument
from utils import *
import sys


def find_most_similar_document(model : Doc2Vec, document : TaggedDocument):
    print(model.docvecs.most_similar(model.infer_vector(document.words)))

# test_doc = get_tagged_documents_reuters("test")[0]
#
model = Doc2Vec.load(sys.argv[1])

print("loaded model")

articles = articles_from_reuters("test")

chosen = articles[int(sys.argv[2])]

print("loaded routers corpus")

similar = find_n_most_similar_articles(model, articles, chosen, 3)

print("\nCHOSEN ARTICLE:")
print(chosen)


print("\nMOST SIMILAR:")
for s in similar[1:]:
    print("\n############# (similarity " + str(s[0]) + ")")
    print(s[1])










