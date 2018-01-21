import argparse
import itertools

import sys

from utils import Article, vector_similarity
from gensim.models.doc2vec import Doc2Vec

INPUT_DELIMITER = '\t'
OUTPUT_DELIMITER = '\t'

parser = argparse.ArgumentParser()
parser.add_argument("model", help="File with doc2vec model")
parser.add_argument("infile", help="Input: csv delimited by '/' with 2 columns: id, text. Without headers")
parser.add_argument("outfile", help="Name of output file")
args = parser.parse_args()

vectors_dict = {}

model = Doc2Vec.load(args.model)

with open(args.infile, 'r') as infile:
    for line in list(infile):
        uid, text = line.strip().split(INPUT_DELIMITER)
        vectors_dict[uid] = model.infer_vector(Article(text).words())

uids_combinations = list(itertools.combinations(vectors_dict.keys(), 2))

i = 0
combinations = len(uids_combinations)

outfile = open(args.outfile, 'w')


for uids in uids_combinations:
    i += 1
    similarity = vector_similarity(vectors_dict[uids[0]], vectors_dict[uids[1]])
    outfile.write(OUTPUT_DELIMITER.join((uids[0], uids[1], str(similarity))) + "\n")
    print("[" + str(i) + "/" + str(combinations) + "]" + str(uids) + ": " + str(similarity), file=sys.stderr)

