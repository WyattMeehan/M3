# DNN model to learn the relation between signal strength and location

# runs Combine.py first

import numpy as np
import tensorflow as tf

def main():
    
    # loads data
    data = np.loadtxt('./data/Localization/data.csv')
    print(np.shape(data)[1])

main()