#!/bin/bash

MS_OUTPUT=$(curl -s "http://localhost:6002/cache/get/$1")
echo "Microservice result = $MS_OUTPUT"

RESULT1=`echo "get $1" | redis-cli -p 8102 -h 172.18.0.101`
RESULT2=`echo "get $1" | redis-cli -p 8102 -h 172.18.0.102`
RESULT3=`echo "get $1" | redis-cli -p 8102 -h 172.18.0.103`

echo "172.18.0.101 = $RESULT1"
echo "172.18.0.102 = $RESULT2"
echo "172.18.0.103 = $RESULT3"

