#!/bin/bash

export EC2_AVAILABILITY_ZONE="rack1" ; export EC2_REGION="rack1" ; 
export DYNOMITE_SEEDS="172.18.0.101:8102:rack1:dc:100|172.18.0.102:8102:rack2:dc:100|172.18.0.103:8102:rack3:dc:100"
export DYNOMITE_DW_SEEDS="172.18.0.201:8102:rack1:dc:100|172.18.0.202:8102:rack2:dc:100|172.18.0.203:8102:rack3:dc:100"

./gradlew -DDEBUG=true run
#./gradlew run -Ddyno.dynomiteCluster.dualwrite.enabled="true" -Ddyno.dynomiteCluster.dualwrite.cluster="dynomiteCluster" -Ddyno.dynomiteCluster.dualwrite.percentage="100"
#./gradlew run
