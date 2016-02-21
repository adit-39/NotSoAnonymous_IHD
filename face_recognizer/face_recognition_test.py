#!/usr/bin/python

# Import the required modules
import cv2, sys
import numpy as np
from PIL import Image
import pickle

cascadePath = "haarcascade_frontalface_default.xml"
faceCascade = cv2.CascadeClassifier(cascadePath)

labels_dict = pickle.load(open("label_mapping.pkl", "rb"))

recognizer = cv2.face.createLBPHFaceRecognizer()
recognizer.load('face_rco_model')

def predictPersons(imgFile):
    people = []
    predict_image_pil = Image.open(imgFile).convert('L')
    predict_image = np.array(predict_image_pil, 'uint8')
    faces = faceCascade.detectMultiScale(predict_image)
    for (x, y, w, h) in faces:
        #print "hi"
        nbr_predicted, conf = recognizer.predict(predict_image[y: y + h, x: x + w])
        for k in labels_dict.keys():
            if labels_dict[k] == nbr_predicted:
                #print k
                people.append(k)
    return people

if __name__ == '__main__':
    person = predictPersons(sys.argv[1])