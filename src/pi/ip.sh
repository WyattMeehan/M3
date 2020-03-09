#!/bin/bash
echo 1_ > log.txt
date >> log.txt
ifconfig >> log.txt
curl --data-binary "@log.txt" ceclnx01.cec.miamioh.edu/~lean2/pi
