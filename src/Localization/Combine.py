# Combine data from pis (from Extraction output) and data from AP (data/AP/our) 
# using MAC addresses and time

import os
import json
import numpy as np


## PARAMETERS


# how long is a timestamp in seconds
seconds = 10


## VARIABLES (don't change)

# AP data
data = []


# converts time to integer
# computes index of timeslot
def compute(time):
    h = int(time[11:13])
    m = int(time[14:16])
    s = int(time[17:19])
    sec = h * 3600 + m * 60 + s
    return int(sec / seconds)

# reads data from pis (from Extraction output)
# matches with data from AP through timeslot
def read_pi():
    path = './data/Extraction/Output/'

    # index of file
    count = 0

    # result
    result = np.zeros((7, 0))

    for file in os.listdir(path):
        if file != 'Exclusion.txt':

            # gets all files in pis data folder
            with open(path + file, 'r') as data_file:
                line = data_file.readline()
                while line:

                    # gets MAC address
                    parts = line.split('\t')
                    address = parts[1]

                    # checks if MAC address is in AP data
                    addresses = data[count]
                    if address in addresses:

                        # gets timeslot
                        time = parts[0]
                        slot = compute(time)

                        # matches with AP data
                        dictionary = addresses[address]
                        if slot in dictionary:

                            print(address + ' ' + time + ' ' + str(dictionary[slot]))

                            # signal strength for pi 5 includes line break character
                            parts[6] = parts[6][0:-1]
                            strength = parts[2:]

                            # formats current example
                            strength = np.array(strength)
                            location = dictionary[slot]
                            location = np.array(location)
                            example = np.concatenate((strength.T, location.T))
                            example = np.reshape(example, (7, 1))
                            result = np.concatenate((result, example), axis=1)

                    line = data_file.readline()
            count = count + 1
    return result.astype(np.float)

# reads AP data to get MAC addresses, time and location
# for each MAC address, saves locations in timeslots
def read_AP():

    path = './data/AP/our/'
    for file in os.listdir(path):


        # dictionary of MAC addresses and location, time
        addresses = {}

        # gets all files in AP data folder
        with open(path + file, 'r') as data_file:
            line = data_file.readline()
            while line:

                # excludes all blank lines
                if len(line) > 1:

                    # interprets data in JSON format
                    devices = json.loads(line)
                    for device in devices:
                        address = device['macAddress']
                        time = device['lastSeen']
                        location = device['locationCoordinate']
                        x_coor = location['x']
                        y_coor = location['y']
                        sec = compute(time)

                        # timeslot
                        slot = compute(time)

                        # adds time and location data to a MAC address
                        if address in addresses:
                            dictionary = addresses[address]
                            dictionary[slot] = (x_coor, y_coor)
                            addresses[address] = dictionary

                        # or appends a new MAC address to the list
                        else:
                            dictionary = {slot : (x_coor, y_coor)}
                            addresses[address] = dictionary

                line = data_file.readline()
        
        # adds data from current date to list of all data
        data.append(addresses)

def main():
    read_AP()
    result = read_pi()
    print(np.shape(result))

main()