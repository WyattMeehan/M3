# fixs time differences and save to new file

from datetime import datetime, timedelta

# data path
path = './data/pidata/'

# list of pis needed to be fixed
pis = []

def fix(no):
    no_str = str(no)
    with open(path + no_str + '.txt', 'w') as out:
        with open(path + 'pi' + no_str + '.txt') as inp:
            for line in inp:

                # time format
                format = '%Y-%m-%d %H:%M:%S.%f'

                # extracts time
                line = line.strip('\n').split('\t')
                time = datetime.strptime(line[0].replace('T', ' '), format)

                # removes time differences
                time = time - timedelta(hours=5)

                # writes new time
                out.write(time.strftime(format) + '\t' + line[1] + '\t' + line[2] + '\n')

def main():
    for pi in pis:
        fix(pi)

main()