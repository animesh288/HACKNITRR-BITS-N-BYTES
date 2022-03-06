from flask import Flask, request, jsonify
import pickle
import re

from collections import Counter
from sklearn.feature_extraction import DictVectorizer

vectorizer = DictVectorizer(sparse=True)

model = pickle.load(open('gridtrylocal.pkl', 'rb'))

app = Flask(__name__)


@app.route('/')
def index():
    return "Conversation Sentiments"


@app.route('/predict', methods=['POST'])
def predict():
    msg = request.form.get('msg')

    features = create_feature(msg, nrange=(1, 4))
    pred = msg
    features = vectorizer.transform(features)
    pred = features.sum()

    pred = model.predict(features)[0]

    return jsonify({'msg': str(pred)})

def ngram(token, n):
    output = []
    for i in range(n - 1, len(token)):
        ngram = ' '.join(token[i - n + 1:i + 1])
        output.append(ngram)
    return output


def create_feature(text, nrange=(1, 1)):
    text_features = []
    text = text.lower()

    # 1. treat alphanumeric characters as word tokens
    # Since tweets contain #, we keep it as a feature
    # Then, extract all ngram lengths
    text_alphanum = re.sub('[^a-z0-9#]', ' ', text)
    for n in range(nrange[0], nrange[1] + 1):
        text_features += ngram(text_alphanum.split(), n)

    # 2. treat punctuations as word token
    text_punc = re.sub('[a-z0-9]', ' ', text)
    text_features += ngram(text_punc.split(), 1)

    # 3. Return a dictinaory whose keys are the list of elements
    # and their values are the number of times appearede in the list.
    return Counter(text_features)




if __name__ == '__main__':
    app.run(debug=True)
