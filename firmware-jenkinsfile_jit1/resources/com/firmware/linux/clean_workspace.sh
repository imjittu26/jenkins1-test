#!/bin/bash

# $1 make_how
set -x

pwd
ls ${env.FIRMWARE_TOOLS_PATH}/bin/

echo "Check workspace directory"
echo "WORKSPACE ${env.WORKSPACE}"
ls -lah ${env.WORKSPACE}

# clean up any mess left over from previous.
echo "git clean before make"
rm config.mk || echo "config.mk missing"
$1 clean

# git clean as well.
git clean -fxd
$1 clean
echo "------ Current Config.mk"
cat ./config.mk
echo "------ End Config.mk"

# Print the last 10 commits.
git log --oneline | head -n 10