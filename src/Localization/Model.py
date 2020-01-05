# DNN model to learn the relation between signal strength and location

# runs Combine.py first

import numpy as np
import tensorflow as tf
import matplotlib.pyplot as plt

## PARAMETERS

# test set size
test_size = 2

# dev set size
dev_size = 2

# plot option
plotting = True


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
    fig.scatter(x[0], y[0], color = "blue")
    fig.scatter(x[1], y[1], color = "green")
    fig.scatter(x[2], y[2], color = "red")

# visualizes location
def visualize(Y_train, Y_dev, Y_test):
    x_train, y_train = seperate(Y_train)
    x_dev, y_dev = seperate(Y_dev)
    x_test, y_test = seperate(Y_test)
    _, (fig0, fig1, fig2) = plt.subplots(1, 3)

    # basement
    plot(fig0, [x_train[0], x_dev[0], x_test[0]], [y_train[0], y_dev[0], y_test[0]])

    # 1st floor
    plot(fig1, [x_train[1], x_dev[1], x_test[1]], [y_train[1], y_dev[1], y_test[1]])

    # 2nd floor
    plot(fig2, [x_train[2], x_dev[2], x_test[2]], [y_train[2], y_dev[2], y_test[2]])

    plt.show()

# handles data
def handle():

    # loads data
    data = np.loadtxt('./data/Localization/data.csv')

    # total size of dev and train set
    total = test_size + dev_size

    # splits data into train, dev and test set
    test = data[:test_size]
    dev = data[test_size:total]
    train = data[total:]
    print('train set size: ' + str(np.shape(train)[0]))

    # extracts labels
    X_train = train[:,:5]
    Y_train = train[:,5:]
    X_dev = dev[:,:5]
    Y_dev = dev[:,5:]
    X_test = test[:,:5]
    Y_test = test[:,5:]

    # visualizes the locations
    if plotting:
        visualize(Y_train, Y_dev, Y_test)

def main():
    handle()

main()