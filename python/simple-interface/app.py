from flask import Flask, jsonify, render_template, request
import pandas as pd
import numpy as np
from localneo4j import page1, page2
from neo4j_processor import init_process

app = Flask(__name__)


@app.route('/index', methods=['GET'])
def index():
    return render_template('index.html')


@app.route('/page1', methods=['GET'])
def page1():
    if request.method == 'GET':
        label = request.args.get('label')
        key = request.args.get('key')
        value = request.args.get('value')
        node = page1.get_simple_node(label, key, value)
        return render_template('page1.html', nodes=node)


@app.route('/page2')
def index2():
    return render_template('graph_show.html')


@app.route('/schedule')
def home():
    nodes, edges = page2.get_all_nodes_and_edges()
    print(nodes)
    return render_template('schedule.html', nodes=nodes, edges=edges)



# @app.route('/home')
# def home():
#     nodes, edges = init_process.get_all_nodes_and_edges()
#     print(nodes)
#     return render_template('demo2.html', nodes=nodes, edges=edges)
#
#
# @app.route('/mysql')
# def mysql():
#     nodes, edges = for_show.get_for_show()
#     return render_template('demo_mysql.html', nodes=nodes, edges=edges)
#
#
@app.route('/getnodes')
def home3():
    nodes, _ = init_process.get_all_nodes_and_edges();
    print(nodes)
    return nodes


if __name__ == '__main__':
    app.run(debug=True)
