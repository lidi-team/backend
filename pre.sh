#!/bin/bash
set +x
export A=$(lsof -t -i:8082)
kill $A