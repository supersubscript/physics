#!/usr/bin/python2

import string, sys, csv, numpy, os, math 

outFile = sys.argv[2]
inFile = sys.argv[1]

with open(inFile, 'r') as f:
    with open(outFile, 'w') as fout:
        rows = f.readlines()
        seen = set()
        for row in rows:
            columns = row.split()
            pair = columns[0] + columns[1]
            if pair not in seen:
                travelers = 0
                seen.add(pair)
                for innerrow in rows:
                    innercol = innerrow.split() 
                    if (innercol[0] + innercol[1]) == pair or (innercol[1] ==
                    columns[0] and innercol[0] == columns[1]):
                        travelers = travelers + float(innercol[2])
                fout.write(str(columns[0]) + "\t" + str(columns[1]) + "\t"
                    + str(travelers/2) + "\n")

    with open(outFile, 'r') as fout:
        rows = fout.readlines()
        rows.sort()
        fout.close()
        with open(outFile, 'w') as ffout:
            ffout.writelines(rows)
    
