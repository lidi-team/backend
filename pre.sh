#!/bin/bash
set +x
# Add this as the 1st build Exec shell
# Only for situations where you have 1 and only 1 executor per machine
# And you don't care about processes being left running after the job run ends

# Look for processes that have a BUILD_ID env var
# that is NOT the same as the current job's BUILD_ID
# nor same as dontKillMe

echo "Killing orphan spawned processes..."
PID_SELF=$$
for PID in $(ps -eo pid,command -u ${USER} | grep -v grep | tail -n+2 | awk '{print $1}' | grep -v ${PID_SELF}); do
  cat /proc/${PID}/environ 2>/dev/null | \
    grep "BUILD_ID=" | \
    grep -v "BUILD_ID=dontKillMe" | \
    grep -v "BUILD_ID=${BUILD_ID}" -q && \
    echo "Killing $(ps -p ${PID} | tail -1)" && \
    kill -9 ${PID}
done || true