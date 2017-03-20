#!/bin/bash

let NUM=($2/10 + 2)

sed "$NUM q;d" $1 | cut -d$'\t' -f2- > temp.dat
transpose_file.py temp.dat temp2.dat
histogram_from_file.py temp2.dat > $3
rm temp.dat temp2.dat

