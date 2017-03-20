reset
#set t pngcairo color enhanced font 'Verdana, 10' size 700,400
load 'parula.pal'
set t postscript eps enhanced  size 11cm,7.5cm color colortext font 'Helvetica,24'
#set t wxt
#compl="LINEAR"

set o "complexities.eps"

set border 3
set yrange [0:100]
set xrange [0:100]
set ytics 25 nomirror
set xtics 25 nomirror
set xlabel "Distance"
set ylabel "Modified distance"

set key nobox top left
load 'parula.pal'

set angles radians
linear(x) = x
ladder(x) = (floor(x/11)*11 + 11./2.)  
ssin(x)   = (x + 11.*sin(x*pi/11.))
hsin(x)   = (x + 2*11.*abs(sin(x*pi/(2*11.))))

plot\
   linear(x)   lt 1 lw 3         t  'Linear',\
   ladder(x)   lt 2 lw 3 dt '--' t  'Stairs',\
   ssin(x)     lt 3 lw 3 dt '._' t  'Moguls',\
   hsin(x)     lt 4 lw 3 dt '__' t  'Sharktooth'
   
set o
set terminal wxt
