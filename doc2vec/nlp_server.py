import json

import numpy as np
from flask import Flask
from flask_restful import Resource, Api, reqparse, abort
import sys
from article2vec import Article2Vec
from utils import Article, find_n_most_similar_vectors


app = Flask(__name__)
api = Api(app)


class Vector(Resource):
    def get(self):
        args = parser.parse_args()

        if args['text'] is None:
            abort(404, message="Text field empty")

        article = Article(args['text'])
        return {'vector': article2vec.vector(article)}


class Similar(Resource):

    def post(self):

        args = parser.parse_args()

        if args['json'] is None:
            abort(404, message="son field empty")

        request_json = json.loads(args['json'])
        vectors = request_json['vectors']
        query_vector_id = request_json['query_id']

        for vid in vectors:
            vectors[vid] = np.array(vectors[vid])

        return find_n_most_similar_vectors(vectors, query_vector_id)

parser = reqparse.RequestParser()
parser.add_argument('text')
parser.add_argument('json')

api.add_resource(Vector, '/vector')
api.add_resource(Similar, '/similar')

if __name__ == '__main__':

    article2vec = Article2Vec(sys.argv[1])
    app.run(debug=True)

