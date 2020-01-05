#!/bin/bash
echo "updating pip"
python -m pip install --upgrade pip
echo "installing numpy"
python -m pip install numpy
echo "installing tensorflow"
python -m pip install tensorflow-gpu
echo "installing matplotlib"
python -m pip install matplotlib