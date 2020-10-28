#!/bin/bash
FILE=./target/api-0.0.1-SNAPSHOT.jar
if test -f "$FILE"; then
  echo "file exists"
  rm -rf ./target/api-0.0.1-SNAPSHOT.jar
  docker container stop my-backend
  docker container prune -f
else
  echo "file does not exist"
fi
