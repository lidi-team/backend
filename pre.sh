#!/bin/bash
set +x
kill $(lsof -t -i:8082)