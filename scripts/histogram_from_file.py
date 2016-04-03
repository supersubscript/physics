#!/usr/bin/env python2

import os
import numpy as np
import argparse

def parse_arg():
    "Use argparse to parse the arguments from the command line"

    descrip = "Make a histogram of data from a column (default: first) in a file"
    parser = argparse.ArgumentParser(description=descrip)

    parser.add_argument('file')
    parser.add_argument('-b','--bins', help='bins to use', required=False)
    parser.add_argument('-c','--col',  help='use col (0 indexed)', required=False, default = 0)
    parser.add_argument('-l','--log',  help='use log spacing on data', required=False, dest='log', action='store_true')
    parser.set_defaults(log=False)
    args = parser.parse_args()
    return args


def main(args):

    if os.stat(args.file).st_size == 0:
        print "Can not do a histogram on an empty file!"
        return -1

    data = np.loadtxt(args.file, comments='#', usecols={int(args.col)})
    print("# Data from: %s, mean: %s, median: %s, variance: %s" %
          (args.file, np.mean(data), np.median(data), np.var(data)))

    bins = np.sqrt(len(data))
    if args.bins is not None:
        bins = int(args.bins)

    if args.log:
        logdata = [np.log(i) for i in data if i > 0]
        f, x = np.histogram(logdata, bins=bins)
    else:
        nr_bins = 9
        low_range =-4
        high_range = 4

        my_bins = np.logspace(low_range, high_range, num=nr_bins,base=2)
        bins = np.r_[0, my_bins,np.inf]
        f, x = np.histogram(data, bins)

    # Normalize:
    tot_number = np.sum(f) # total count

    print("# value:\t fraq:\t count:")
    for i in range(len(f)):
        print("%s\t%f\t%.0f" % (x[i],f[i]/float(tot_number),f[i]))

if __name__ == "__main__":
    args = parse_arg()
    main(args)
