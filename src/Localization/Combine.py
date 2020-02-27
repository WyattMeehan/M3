# Combine data from pis (from Extraction output) and data from AP (data/AP/our) 
# using MAC addresses and time
# saves combined data set at data/Localization/data.csv

## REQUIREMENTS (subjects to change as software got updated)
# Microsoft Visual C++ 14.0 or above
# for tensorflow: https://www.tensorflow.org/install/gpu#hardware_requirements
# missing cudart64_100.dll problem: https://www.joe0.com/2019/10/19/how-resolve-tensorflow-2-0-error-could-not-load-dynamic-library-cudart64_100-dll-dlerror-cudart64_100-dll-not-found/
# missing cublas64_100.dll problem: in the same folder as the above problem, copy cublas64_10.dll to cublas64_100.dll
# then runs dependencies.sh to install required package(s)

import os
import json
#import numpy as np
import pandas as pd

## PARAMETERS

# how long is a timestamp in seconds
seconds = 10


## VARIABLES (don't change)


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
def read_pi(path, addresses):

    result = []

    with open(path) as data_file:
        line = data_file.readline()
        while line:

            # gets MAC address
            parts = line.split('\t')
            address = parts[1]

            # checks if MAC address is in AP data
            if address in addresses:

                # gets timeslot
                time = parts[0]
                slot = compute(time)

                # matches with AP data
                dictionary = addresses[address]
                if slot in dictionary:

                    # signal strength for pi 5 includes line break character
                    parts[6] = parts[6][0:-1]
                    strength = parts[2:]

                    # adds current instance into result list
                    # example = np.concatenate((np.array(strength), np.array(dictionary[slot])))
                    # example = np.reshape(example, (1, 8))
                    # result = np.concatenate((result, example))
                    parts.extend(dictionary[slot])
                    for index in range(2, len(parts)):
                        parts[index] = int(parts[index])
                    result.append(parts)

            # proceeds to next line
            line = data_file.readline()

    return result

# reads AP data to get MAC addresses, time and location
# for each MAC address, saves locations in timeslots
def read_AP(path):

    # dictionary of MAC addresses and location, time
    addresses = {}

    with open(path) as data_file:
        line = data_file.readline()
        while line:

            # excludes all blank lines
            if len(line) > 1:

                # interprets data in JSON format
                devices = json.loads(line)
                for device in devices:

                    # filters only data in Benton
                    details = device['hierarchyDetails']
                    #if details['building']['name'] == 'Benton Hall':

                    address = device['macAddress']
                    time = device['lastSeen']
                    location = device['locationCoordinate']
                    x_coor = location['x']
                    y_coor = location['y']

                    # floor
                    floor = details['floor']['name']
                    if floor == '2nd Floor':
                        floor_number = 2
                    elif floor == '1st Floor':
                        floor_number = 1
                    else:
                        floor_number = 0

                    # timeslot
                    slot = compute(time)

                    # retrieves a MAC address's data from the dictionary of MAC addresses
                    if address in addresses:
                        dictionary = addresses[address]
                        dictionary[slot] = [x_coor, y_coor, floor_number]

                    # or creates a new one
                    else:
                        dictionary = {slot : [x_coor, y_coor, floor_number]}

                    # saves it back to the dictionary
                    addresses[address] = dictionary

            # proceeds to next line
            line = data_file.readline()
    
    return addresses

def main():

    # result data
    data = []

    pi_path = './data/Extraction/Output/pi.txt'
    AP_path = './data/AP/Benton/'
    result = np.zeros((0, 8))

    # finds same day files in 2 data folder
    # for file in os.listdir(pi_path):
    #     if file in os.listdir(AP_path):
    for floor in range(3):
        floor_str = str(floor)
        print('reading floor ' + floor_str)
        addresses = read_AP(AP_path + floor_str + '/21-02-2020.txt')
        data.extend(read_pi(pi_path, addresses))
        #result = np.concatenate((result, day))

    # result data frame
    frame = pd.DataFrame(data = data, columns = ['time', 'MAC address', 'pi1', 'pi2', 'pi3', 'pi4', 'pi5', 'x', 'y', 'floor'])

    # number of matches
    print(str(len(frame.index)) + ' matches')

    # saves data to file
    #np.savetxt('./data/Localization/data.csv', result)
    frame.to_csv('./data/Localization/data.csv', index=False)

main()