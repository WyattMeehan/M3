# produce location input from signal input

import numpy as np
import keras
from keras.models import model_from_json
import matplotlib.pyplot as plt

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

def plot(fig):
    fig.set_aspect('equal')
    fig.set_xlim([0, 250])
    fig.set_ylim([0, 250])

# tests
def main():
    X = np.array([[-121,-121,-121,-121,-121]])
    result = compute(X)
    print(result)

    # plots
    _, fig = plt.subplots(1, 3)
    plot(fig[0])
    plot(fig[1])
    plot(fig[2])
    fig[0].set_title('basement')
    fig[1].set_title('1st floor')
    fig[2].set_title('2nd floor')

    # basement
    if result[2][0] < 0.5:
        fig[0].scatter(result[0][0], result[1][0])

    # 1st floor
    elif result[2][0] < 1.5:
        fig[1].scatter(result[0][0], result[1][0])

    # 2nd floor
    else:
        fig[2].scatter(result[0][0], result[1][0])

    plt.show()

main()
