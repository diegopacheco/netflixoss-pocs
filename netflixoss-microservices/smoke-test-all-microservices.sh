#!/bin/bash

echo "6/6= $(curl -s http://127.0.0.1:6001/math/div/6/6)"
echo "6*6= $(curl -s http://127.0.0.1:6002/math/mul/6/6)"
echo "6-6= $(curl -s http://127.0.0.1:6003/math/sub/6/6)"
echo "6+6= $(curl -s http://127.0.0.1:6004/math/sum/6/6)"

