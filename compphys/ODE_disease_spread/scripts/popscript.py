#!/usr/bin/python2

import string, sys, csv, numpy, os, math 

datdata= sys.argv[1]
popdata = sys.argv[2]
outFile = sys.argv[3]

with open(datdata, 'r') as d, open(popdata, 'r') as p, open(outFile, 'w') as o:
    dataLines = d.readlines()
    popLines = p.readlines()
    for drow in dataLines:
        dcols = drow.split()
        for prow in popLines:
            pcols = prow.split()
            if dcols[0] == pcols[0]:
                o.write(dcols[0] + "\t" + dcols[1] + "\t" + dcols[2] + "\t" +
                        pcols[1] + "\n")
        
