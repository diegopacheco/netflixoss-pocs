#!/bin/bash

service redis-server start &

mkdir /var/log/dynomite/

/dynomite/src/dynomite --conf-file=/dynomite/conf/redis_cluster_$DYNOMITE_NODE.yml -o /var/log/dynomite/dynomite_log.txt
