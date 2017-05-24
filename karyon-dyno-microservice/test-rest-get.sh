#!/bin/bash

MS_OUTPUT=$(curl -s "http://localhost:6002/cache/get/$1")
echo "Microservice result = $MS_OUTPUT"

