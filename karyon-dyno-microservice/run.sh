#!/bin/bash

export DYNOMITE_SEEDS="172.18.0.101:8101:rack1:dc:100|172.18.0.102:8101:rack2:dc:100|172.18.0.103:8101:rack3:dc:100" ; ./gradlew run -D"dyno.dynomiteCluster.retryPolicy=RetryNTimes:3:true"
