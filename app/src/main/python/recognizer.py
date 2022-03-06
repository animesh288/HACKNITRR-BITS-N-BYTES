import speech_recognition as sr
import sys

r = sr.Recognizer()

# open the file
def main():
    filename="/storage/emulated/0/sample1.flac"
    with sr.AudioFile(filename) as source:
        # listen for the data (load audio to memory)
        audio_data = r.record(source)
        # recognize (convert from speech to text)
        text = r.recognize_google(audio_data)
        return text