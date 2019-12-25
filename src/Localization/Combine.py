## Combine data from pis (from Extraction output) and data from AP (data/AP/our) using MAC addresses and time

import os
import json


#### PARAMETERS


## how long is a timestamp in seconds
seconds = 10


#### VARIABLES (don't change)

## dictionary of MAC addresses and data
addresses = {}


## reads data from pis (from Extraction output)
## seperates the data into timestamps
def read_pi():
    x = 2

## reads AP data to get MAC addresses, time and location
def read_AP():

    ## gets all files in AP data folder
    ## for each MAC address, saves locations in timeslots
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

                        ## converts time to integer
                        h = int(time[11:13])
                        m = int(time[14:16])
                        s = int(time[17:19])
                        sec = h * 3600 + m * 60 + s

                        ## timeslot
                        slot = int(sec / seconds)

                        ## adds time and location data to a MAC address
                        if address in addresses:
                            dictionary = addresses[address]
                            dictionary[slot] = (x_coor, y_coor)

                        ## or appends a new MAC address to the list
                        else:
                            dictionary = {time : (x_coor, y_coor)}
                            addresses[slot] = dictionary

                line = data_file.readline()

def main():
    read_AP()

main()