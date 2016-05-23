#!/bin/bash

sed -i 's/@rack/$DYNOMITE_RACK/g'     /etc/dynomite/seeds.list
sed -i 's/@tokens/$DYNOMITE_TOKENS/g' /etc/dynomite/seeds.list

node scripts/Florida/florida.js  

