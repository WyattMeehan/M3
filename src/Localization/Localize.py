# produce location input from signal input

import numpy as np
import keras
from keras.models import model_from_json

# loads model
# computes location
def compute(signals):

    # loads model
    with open('./data/Localization/model.json', 'r') as file:
        model = model_from_json(file.read())
    model.load_weights('./data/Localization/weights.h5')

    # computes location
    Y_pred = model.predict(signals)
    return Y_pred

# tests
def main():
    X = np.zeros((1, 5))
    print(compute(X))

main()
