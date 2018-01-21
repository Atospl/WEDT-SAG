from nltk.corpus import reuters
from gensim.models.doc2vec import TaggedDocument, Doc2Vec
import sys
from utils import get_tagged_documents_reuters, get_tagged_documents_csv
import argparse

parser = argparse.ArgumentParser()

parser.add_argument("model")
parser.add_argument("--corpus", default='reuters')
parser.add_argument("--csv")
parser.add_argument("--delimiter", default='\t')

args = parser.parse_args()
training_corpus = []

if args.corpus == 'reuters':
    training_corpus += get_tagged_documents_reuters("training")

if args.csv:
    training_corpus += get_tagged_documents_csv(args.csv, args.delimiter)

print(training_corpus[-1])

model = Doc2Vec()

model.build_vocab(training_corpus)
model.train(training_corpus, total_examples=len(training_corpus), epochs=20)

model.save(args.model)