#!/bin/bash

function traverse() {
    for directory in $(ls "$1")
    do
        if [[ -d ${1}/${directory} ]]; then
            echo "Processing ${1}/${directory}... "
            cd "${1}/${directory}"
            #gradle wrapper
            ./gradlew build eclipse
            cd ../
        fi
    done
}

function main() {
    traverse "$1"
}

main "$(pwd)"
