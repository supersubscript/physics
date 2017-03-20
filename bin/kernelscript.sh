#!/bin/bash

let NUM=($2/10 + 2)

rm $3
sed "$NUM q;d" $1 | cut -d$'\t' -f2- > temporaryUselessFile.dat
transpose_file.py temporaryUselessFile.dat $3
rm temporaryUselessFile.dat 
