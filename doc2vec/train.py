from nltk.corpus import reuters
from gensim.models.doc2vec import TaggedDocument, Doc2Vec
import sys
from utils import get_tagged_documents_reuters


training_corpus = get_tagged_documents_reuters("training")

testing_corpus = get_tagged_documents_reuters("testing")

model = Doc2Vec()

model.build_vocab(training_corpus)
model.train(training_corpus, total_examples=len(training_corpus), epochs=20)

model.save(sys.argv[1])