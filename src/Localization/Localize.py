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

# def plot(fig):
#     fig.set_aspect('equal')
#     fig.set_xlim([0, 250])
#     fig.set_ylim([0, 120])

# tests
def main():
    X = np.array([[-69,-77,-63,-65,-47]])
    result = compute(X)
    print(result)

    # plots
    floor_0 = plt.imread('./img/0.jpg')
    floor_1 = plt.imread('./img/1.jpg')
    floor_2 = plt.imread('./img/2.jpg')
    _, fig = plt.subplots(3, 1, figsize = (12.5, 6))
    # plot(fig[0])
    # plot(fig[1])
    # plot(fig[2])
    # fig[2].set_title('basement')
    # fig[1].set_title('1st floor')
    # fig[0].set_title('2nd floor')
    fig[2].imshow(floor_0, extent = (0, 250, 0, 120))
    fig[1].imshow(floor_1, extent = (0, 250, 0, 120))
    fig[0].imshow(floor_2, extent = (0, 250, 0, 120))

    # basement
    if result[2][0] < 0.5:
        fig[2].scatter(result[0][0], result[1][0])

    # 1st floor
    elif result[2][0] < 1.5:
        fig[1].scatter(result[0][0], result[1][0])

    # 2nd floor
    else:
        fig[0].scatter(result[0][0], result[1][0])

    plt.show()

main()
