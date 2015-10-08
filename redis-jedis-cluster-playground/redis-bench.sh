#!/bin/bash

git pull ; ./gradlew clean build jar
java -jar ./build/libs/redis-jedis-cluster-playground-1.0.jar
