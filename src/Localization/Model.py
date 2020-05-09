# DNN model to learn the relation between signal strength and location
# set plot option to True for visualization of devices' locations

# runs Combine.py first

import numpy as np
import keras
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from keras.models import Model
from keras.layers import Dense, Input
from keras.losses import mse
from keras.optimizers import SGD
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.utils import shuffle
import keras.backend as K
import math
from keras.layers.advanced_activations import PReLU

## PARAMETERS

# test set size
test_size = 0.2

# dev set size
# dev_size = 2

# plot option
plotting = True

# model dimesion
coor_dimension = [4, 4]
floor_dimension = [4, 8, 8]

# number of epochs
no = 200

# batch size
size = 8

# weight for floor (compare to weight for coordinate)
weight = 90000

# optimizer
optimizer = 'nadam'

# seperates floors
def seperate(set):
    x0 = []
    x1 = []
    x2 = []
    y0 = []
    y1 = []
    y2 = []
    for row in set:
        if row[2] == 0:
            x0.append(row[0])
            y0.append(row[1])
        elif row[2] == 1:
            x1.append(row[0])
            y1.append(row[1])
        else:
            x2.append(row[0])
            y2.append(row[1])
    return [x0, x1, x2], [y0, y1, y2]


# plots each floor
def plot(fig, x, y):
    # fig.set_aspect('equal')
    # fig.set_xlim([0, 250])
    # fig.set_ylim([0, 250])
    fig.scatter(x[0], y[0], color = 'blue')
    fig.scatter(x[1], y[1], color = 'green')

# visualizes location
def visualize(Y_train, Y_test):

    floor_0 = plt.imread('./img/0.jpg')
    floor_1 = plt.imread('./img/1.jpg')
    floor_2 = plt.imread('./img/2.jpg')
    
    # seperates floor
    Y_train_0 = Y_train[Y_train['floor'] == 0]
    Y_train_1 = Y_train[Y_train['floor'] == 1]
    Y_train_2 = Y_train[Y_train['floor'] == 2]
    Y_test_0 = Y_test[Y_test['floor'] == 0]
    Y_test_1 = Y_test[Y_test['floor'] == 1]
    Y_test_2 = Y_test[Y_test['floor'] == 2]

    # x_train, y_train = seperate(Y_train)
    # x_dev, y_dev = seperate(Y_dev)
    # x_test, y_test = seperate(Y_test)
    x_train_0 = Y_train_0['x'].tolist()
    x_train_1 = Y_train_1['x'].tolist()
    x_train_2 = Y_train_2['x'].tolist()
    y_train_0 = Y_train_0['y'].tolist()
    y_train_1 = Y_train_1['y'].tolist()
    y_train_2 = Y_train_2['y'].tolist()
    x_test_0 = Y_test_0['x'].tolist()
    x_test_1 = Y_test_1['x'].tolist()
    x_test_2 = Y_test_2['x'].tolist()
    y_test_0 = Y_test_0['y'].tolist()
    y_test_1 = Y_test_1['y'].tolist()
    y_test_2 = Y_test_2['y'].tolist()

    _, (fig2, fig1, fig0) = plt.subplots(3, 1)

    fig2.imshow(floor_0, extent = (0, 250, 0, 120))
    fig1.imshow(floor_1, extent = (0, 250, 0, 120))
    fig0.imshow(floor_2, extent = (0, 250, 0, 120))

    # # basement
    plot(fig0, [x_train_0,  x_test_0], [y_train_0, y_test_0])

    # # 1st floor
    plot(fig1, [x_train_1,  x_test_1], [y_train_1, y_test_1])

    # # 2nd floor
    plot(fig2, [x_train_2,  x_test_2], [y_train_2, y_test_2])

    plt.show()

# pops multiple columns from pandas data frame
def multi_pop(frame):
    target = frame[['x', 'y', 'floor']].copy()
    train = frame.drop(['x', 'y', 'floor'], axis = 1)
    return train, target

# handles data
def handle():

    # loads data
    # data = np.loadtxt('./data/Localization/data.csv')
    data = pd.read_csv('./data/Localization/data.csv', index_col=False)
    data = data.drop(['time', 'MAC address'], axis = 1)

    # total size of dev and train set
    # total = test_size + dev_size

    # shuffles data
    data = shuffle(data)

    # splits data into train, dev and test set
    # test = data[:test_size]
    # dev = data[test_size:total]
    # train = data[total:]
    train, test = train_test_split(data, test_size = test_size)

    # extracts labels
    # X_train = train[:,:5]
    X_train, Y_train = multi_pop(train)
    # X_dev = dev[:,:5]
    # Y_dev = dev[:,5:]
    # X_test = test[:,:5]
    X_test, Y_test = multi_pop(test)

    # visualizes the locations
    if plotting:
        visualize(Y_train, Y_test)

    # normalizes data
    # scaler = StandardScaler()
    # X_train = scaler.fit_transform(X_train)
    # X_dev = scaler.transform(X_dev)
    # X_test = scaler.transform(X_test)

    return X_train, Y_train, X_test, Y_test

# transforms pandas data frame to numpy array
def transform(frame, no):
    array = frame.values
    #print(array)
    array = np.reshape(array, (len(frame.index), no))
    return array

# builds model
def build(X_train, Y_train, X_test, Y_test):

    # number of train samples
    # number = np.shape(X[0])[0]
    number = len(Y_train.index)
    print(str(number) + ' rows')

    # transforms pandas data frame to numpy array
    X_train = transform(X_train, 5)
    Y_train = transform(Y_train, 3)
    X_test = transform(X_test, 5)
    Y_test = transform(Y_test, 3)

    # input layer
    input_layer = Input((5,))
    x_layer = Input((1,))
    y_layer = Input((1,))
    floor_layer = Input((1,))

    # hidden layers
    hidden_x = input_layer
    for layer in coor_dimension:
        hidden_x = Dense(layer, kernel_initializer='uniform', activation='linear')(hidden_x)
        hidden_x = PReLU()(hidden_x)
    hidden_y = input_layer
    for layer in coor_dimension:
        hidden_y = Dense(layer, kernel_initializer='uniform', activation='linear')(hidden_y)
        hidden_y = PReLU()(hidden_y)
    hidden_floor = input_layer
    for layer in floor_dimension:
        hidden_floor = Dense(layer, kernel_initializer='uniform', activation='linear')(hidden_floor)
        hidden_floor = PReLU()(hidden_floor)

    # output layers
    output_0 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_0')(hidden_x)
    output_1 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_1')(hidden_y)
    output_2 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_2')(hidden_floor)

    # output weight
    # weight_coor = np.ones((number, ))
    # weight_floor = np.ones((number, )) * weight

    # trains model
    model = Model([input_layer, x_layer, y_layer, floor_layer], [output_0, output_1, output_2])
    #model.compile(loss='mean_squared_error', optimizer=optimizer)
    loss = K.mean(K.sqrt((K.square(output_0 - x_layer) + K.square(output_1 - y_layer)))) + weight * mse(output_2, floor_layer)
    model.add_loss(loss)
    model.compile(optimizer=optimizer)
    print(model.summary())
    model.fit([X_train, Y_train[:,0], Y_train[:,1], Y_train[:,2]], epochs=no, batch_size=size)

    # test model
    evaluation = model.evaluate([X_test, Y_test[:,0], Y_test[:,1], Y_test[:,2]], batch_size=1)
    print('\nevaluation of test set: ')
    print(str(model.metrics_names) + ': ' + str(evaluation))
    dummy = np.array([[0]])
    distance_loss = 0
    floor_loss = 0
    length = X_test.shape[0]
    for index in range(length):
        inp = X_test[index]
        out = Y_test[index]
        prediction = model.predict([np.array([[inp[0], inp[1], inp[2], inp[3], inp[4]]]), dummy, dummy, dummy])
        distance_loss += math.sqrt(math.pow(prediction[0][0] - out[0], 2) + math.pow(prediction[1][0] - out[1], 2))
        floor_loss += abs(prediction[2][0] - out[2])
    print('distance loss: ' + str(distance_loss / length))
    print('floor loss: ' + str(floor_loss / length))
    print('sample prediction: ' + str(model.predict([np.array([[-69,-77,-63,-65,-47]]), dummy, dummy, dummy])))

    # serializes and saves model
    json = model.to_json()
    with open('./data/Localization/model.json', 'w') as file:
        file.write(json)
    model.save_weights('./data/Localization/weights.h5')

def main():
    X_train, Y_train, X_test, Y_test = handle()
    # build(X_train, Y_train, X_test, Y_test)

main()