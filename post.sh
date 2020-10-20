#!/bin/bash
set +x
# Add this as a post-build script i.e. with PostScriptPlugin

# Look for processes that have a BUILD_ID env var
# that is the same as the current job's BUILD_ID
# (ignore current process and grep)

echo "Killing spawned processes..."
PID_SELF=$$
for PID in $(ps -eo pid,command -u ${USER} | grep -v grep | tail -n+2 | awk '{print $1}' | grep -v ${PID_SELF}); do
  grep "BUILD_ID=${BUILD_ID}" /proc/${PID}/environ -q 2>/dev/null && \
    echo "Killing $(ps -p ${PID} | tail -1)" && \
    kill -9 ${PID}
done || true