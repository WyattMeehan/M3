# DNN model to learn the relation between signal strength and location

# runs Combine.py first

import numpy as np
import tensorflow as tf

## PARAMETERS

# test set size
test_size = 2

# dev set size
dev_size = 2

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

def main():
    handle()

main()