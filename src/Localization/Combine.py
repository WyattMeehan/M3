## Combine data from pis (from Extraction output) and data from AP (data/AP/our) using MAC addresses and time

import os
import json


#### PARAMETERS


#### VARIABLES (don't change)

## list of MAC addresses
addresses = []


## reads data from pis (from Extraction output)
## seperates the data into timestamps
def read_pi():
    x = 2

## reads AP data to get MAC addresses, time and location
def read_AP():

    ## gets all files in AP data folder
    path = './data/AP/our/'
    for file in os.listdir(path):
        with open(path + file, 'r') as data_file:
            line = data_file.readline()
            while line:

                ## excludes all blank lines
                if len(line) > 1:

                    ## interprets data in JSON format
                    devices = json.loads(line)
                    for device in devices:
                        address = device['macAddress']
                        time = device['lastSeen']
                        location = device['locationCoordinate']
                        x_coor = location['x']
                        y_coor = location['y']

                        ## adds new MAC address to the list
                        if addresses.index(address) < 0:
                            addresses.append(address)

                line = data_file.readline()

def main():
    read_AP()

main()