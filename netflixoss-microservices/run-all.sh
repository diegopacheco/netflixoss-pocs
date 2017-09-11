#!/bin/bash

./gradlew :calc-microservice:run > calc.log &
./gradlew :div-microservice:run > div.log &
./gradlew :mul-microservice:run > mul.log &
./gradlew :sub-microservice:run > sub.log &
./gradlew :sum-microservice:run > sum.log &
