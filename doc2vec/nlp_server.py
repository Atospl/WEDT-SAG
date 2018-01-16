import json

import numpy as np
from flask import Flask, jsonify
from flask_restful import Resource, Api, reqparse, abort
import sys
from article2vec import Article2Vec
from utils import Article, find_n_most_similar_vectors
import string


app = Flask(__name__)
api = Api(app)


class Vector(Resource):
    def get(self):
        args = parser.parse_args()

        if args['text'] is None:
            abort(404, message="Text field empty")

        article = Article(args['text'])
        resp = {'vector': article2vec.vector(article)}

        resp = app.response_class(
        response=json.dumps(resp),
        status=200,
        mimetype='application/json'
    )
        resp.headers['Connection'] = 'close'
        return resp


class Similar(Resource):

    def post(self):

        args = parser.parse_args()

        if args['json'] is None:
            abort(404, message="son field empty")
        request_json = json.loads(args['json'].replace('\'', '\"'))
        vectors = request_json['vectors']
        query_vector_id = request_json['query_id']

        vectors_dict = {}
        for v in vectors:
            vectors_dict[v['id']] = np.array(v['vector'])
        resp = find_n_most_similar_vectors(vectors_dict, query_vector_id)

        resp = app.response_class(
        response=json.dumps(resp),
        status=200,
        mimetype='application/json'
        )
        resp.headers['Connection'] = 'close'

        return resp
parser = reqparse.RequestParser()
parser.add_argument('text')
parser.add_argument('json')

api.add_resource(Vector, '/vector')
api.add_resource(Similar, '/similar')

if __name__ == '__main__':

    article2vec = Article2Vec(sys.argv[1])
    app.run(debug=True)

