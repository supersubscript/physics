#!/bin/bash
rm 0.dat 10.dat 100.dat 1000.dat 10000.dat 99990.dat
~/bin/extract_and_transpose.sh mutation.dat 0    
~/bin/extract_and_transpose.sh mutation.dat 10   
~/bin/extract_and_transpose.sh mutation.dat 100  
~/bin/extract_and_transpose.sh mutation.dat 1000 
~/bin/extract_and_transpose.sh mutation.dat 10000
~/bin/extract_and_transpose.sh mutation.dat 99990
