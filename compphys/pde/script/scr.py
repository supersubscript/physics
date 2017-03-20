#!/usr/bin/python2

import string, sys, csv, numpy, os, math 

outFile = sys.argv[2]
inFile = sys.argv[1]

with open(inFile, 'r') as f:
    with open(outFile, 'w') as fout:
        rows = f.readlines()
        print "#time \t position \t value"
        time = 0
        for row in rows:
            columns = row.split("\t")[1:]
            pos = 0 
            for i, val in enumerate(columns[1:]):
                fout.write(str(time) + "\t" + str(pos)  + "\t" +  columns[i] +
                        "\t")
                fout.write("\n")
                pos = pos + 1
            fout.write("\n")
            time = time + 1
