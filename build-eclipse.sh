#!/bin/bash

function traverse() {
    for file in $(ls "$1")
    do
        if [[ -d ${1}/${file} ]]; then
            echo "Processing ${1}/${file}... "
            ./gradlew build eclipse
        fi
    done
}

function main() {
    traverse "$1"
}

main "$(pwd)"
