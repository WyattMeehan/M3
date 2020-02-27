# DNN model to learn the relation between signal strength and location
# set plot option to True for visualization of devices' locations

# runs Combine.py first

import numpy as np
import keras
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler
from keras.models import Model
from keras.layers import Dense, Input
import keras.backend as bk
from keras.losses import mse
from keras.optimizers import SGD
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.utils import shuffle
import keras.backend as K

## PARAMETERS

# test set size
test_size = 0.2

# dev set size
# dev_size = 2

# plot option
plotting = True

# model dimesion
dimension = [4]

# number of epochs
no = 10

# batch size
size = 8

# weight for floor (compare to weight for coordinate)
# weight = 400

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
    fig.set_aspect('equal')
    fig.set_xlim([0, 250])
    fig.set_ylim([0, 250])
    fig.scatter(x[0], y[0], color = 'blue')
    fig.scatter(x[1], y[1], color = 'green')

# visualizes location
def visualize(Y_train, Y_test):
    
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

    _, (fig0, fig1, fig2) = plt.subplots(1, 3)

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
    hidden = input_layer
    for layer in dimension:
        hidden = Dense(layer, kernel_initializer='uniform', activation='relu')(hidden)

    # output layers
    output_0 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_0')(hidden)
    output_1 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_1')(hidden)
    output_2 = Dense(1, kernel_initializer='uniform', activation='linear', name = 'output_2')(hidden)

    # optimizer
    optimizer = 'adam'

    # output weight
    # weight_coor = np.ones((number, ))
    # weight_floor = np.ones((number, )) * weight

    # trains model
    model = Model([input_layer, x_layer, y_layer, floor_layer], [output_0, output_1, output_2])
    #model.compile(loss='mean_squared_error', optimizer=optimizer)
    loss = K.mean(((output_0 - x_layer) * (output_0 - x_layer) + (output_1 - y_layer) * (output_1 - y_layer)) 
    * mse(output_2, floor_layer) * mse(output_2, floor_layer))
    model.add_loss(loss)
    model.compile(optimizer=optimizer)
    model.fit([X_train, Y_train[:,0], Y_train[:,1], Y_train[:,2]], epochs=no, batch_size=size)

    # test model
    evaluation = model.evaluate([X_test, Y_test[:,0], Y_test[:,1], Y_test[:,2]])
    print('\nevaluation of test set: ')
    print(str(model.metrics_names) + ': ' + str(evaluation))
    # dummy = np.array([[0]])
    # print(model.predict([np.array([[-69,-77,-63,-65,-47]]), dummy, dummy, dummy]))

    # serializes and saves model
    json = model.to_json()
    with open('./data/Localization/model.json', 'w') as file:
        file.write(json)
    model.save_weights('./data/Localization/weights.h5')

def main():
    X_train, Y_train, X_test, Y_test = handle()
    build(X_train, Y_train, X_test, Y_test)

main()