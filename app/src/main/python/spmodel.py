import torch
import librosa
import numpy as np
import soundfile as sf
from scipy.io import wavfile
from transformers import *



tokenizer = Wav2Vec2Tokenizer.from_pretrained("facebook/wav2vec2-base-960h")
model = Wav2Vec2ForCTC.from_pretrained("facebook/wav2vec2-base-960h")

def mecrm():
    file_name="/storage/emulated/0/sec1.wav"
    data = wavfile.read(file_name)
    framerate = data[0]
    sounddata = data[1]
    time = np.arange(0,len(sounddata))/framerate
    input_audio, _ = librosa.load(file_name, sr=16000)
    input_values = tokenizer(input_audio, return_tensors="pt").input_values
    logits = model(input_values).logits
    predicted_ids = torch.argmax(logits, dim=-1)
    transcription = tokenizer.batch_decode(predicted_ids)[0]
    return transcription