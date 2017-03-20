#!/usr/bin/python2

import string, sys, csv, numpy, os, math 

inFiles = sys.argv[2:]
outFile = sys.argv[1]

seen = set()
with open(outFile, 'w') as fout:
    total = 0
    i = 0
    for inFile in inFiles:
        name = inFile.split("/")
        with open(inFile, 'r') as f:
            lines = f.readlines()[1:]

            for line in lines:
                fields = line.split()
                if name[0] in seen:
                    total = total + float(fields[2])     
                else:
                    seen.add(name[0])
                    fout.write(name[0] + "\t")
                    total = total + float(fields[2])
            if i == 27:
                fout.write(str(total / 52) + "\n")
                i = 0
                total = 0
            else:
                i = i+1
