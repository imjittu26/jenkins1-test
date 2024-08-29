#!/bin/bash

set -x

# --------- START WORKSPACE CHECKS -------------
# Use allocated workspace.
cd $WORKSPACE
echo WORKSPACE $WORKSPACE
ls -lah
FREE_SPACE_GB=$(df  . -B $((1024*1024*1024)) | grep "Filesystem" -v | awk --  '{ print $4 }')

# Check for feee space on the node.
if [ $FREE_SPACE_GB -lt 5 ]; then echo "NOT ENOUGH FREE SPACE TO CONTINUE!"; df . -h; exit 1; fi

# Check and fix ubuntu_ver.
if [ -n "$ubuntu_ver" ]; then export UBUNTU_VER=$ubuntu_ver; fi
# ----------- END WORKSPACE CHECKS -------------