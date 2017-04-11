#!/bin/bash

let NUM=($2/10 + 2)

sed "$NUM q;d" $1 | cut -d$'\t' -f2- > temp.dat
transpose_file.py temp.dat $2.dat
rm temp.dat 
