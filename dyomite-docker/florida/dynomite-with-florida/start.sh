#!/bin/bash

service redis-server start &

mkdir /var/log/dynomite/

sed -i "s/@rack/$DYNOMITE_RACK/g"     /dynomite/conf/dynomite_florida.yml
sed -i "s/@tokens/$DYNOMITE_TOKENS/g" /dynomite/conf/dynomite_florida.yml

/dynomite/src/dynomite --conf-file=/dynomite/conf/dynomite_florida.yml -v11 -M 200000 -o /var/log/dynomite/dynomite_log.txt
