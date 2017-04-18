#!/bin/bash

export DYNOMITE_SEEDS="172.18.0.101:8102:rack1:dc:100|172.18.0.102:8102:rack2:dc:100|172.18.0.103:8102:rack3:dc:100" ; export DYNOMITE_DW_SEEDS="127.0.0.1:8102:rack1:local-dc:100" ; ./gradlew run 
