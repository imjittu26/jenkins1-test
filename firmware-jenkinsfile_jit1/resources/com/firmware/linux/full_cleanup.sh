#!/bin/bash

find /home/jenkins/workspace/workspace/ -maxdepth 1 -ctime +5 -type d  -exec rm -rf {} \;