#!/bin/bash

for item in */
do 
    cd $item
    # do something
    echo `pwd`
    hscript.sh
    cd ..
done

exit
