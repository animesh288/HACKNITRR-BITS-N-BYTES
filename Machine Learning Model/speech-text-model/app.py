import pickle
import joblib
from statistics import mode
import torch
import librosa
import numpy as np
import soundfile as sf
from scipy.io import wavfile
from IPython.display import Audio
from flask import Flask, request, jsonify


#model = joblib.load('C:\Users\krish\Documents\E-Cell Hackthon\letstryonce\joba.pkl')

#with open('C:\Users\krish\Documents\E-Cell Hackthon\letstryonce\tokenizer.pickle', 'rb') as handle:
#    tokenizer = pickle.load(handle)

#tokenizer = pickle.load(open('tokenizer.pickle', 'rb'))
model = pickle.load(open('joba.pkl', 'rb'))



app = Flask(__name__)

@app.route('/')
def index():
    return "ek ummid started1"


@app.route('/predict', methods=['POST'])
def predict():
    linkf = request.form.get('linkf')
    
    data = wavfile.read(linkf)
    framerate = data[0]
    sounddata = data[1]
    time = np.arange(0,len(sounddata))/framerate
    input_audio, _ = librosa.load(linkf, sr=16000)
    input_values = tokenizer(input_audio, return_tensors="pt").input_values
    logits = model(input_values).logits
    predicted_ids = torch.argmax(logits, dim=-1)
    transcription = tokenizer.batch_decode(predicted_ids)[0]
    return jsonify({'msg': str(transcription)})


if __name__ == '__main__':
    app.run(debug=True)
