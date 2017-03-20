reset
#set t pngcairo color enhanced font 'Verdana, 10' size 700,400
load 'parula.pal'
set t postscript eps enhanced  size 11cm,7.5cm color colortext font 'Helvetica,24'
#set t wxt
#compl="LINEAR"

set o "mutrates_hard.eps"

set border 3
set yrange [0:400]
set xrange [0:4]
set ytics 100 nomirror
set xtics ("x 1/10" 0, "x 1/2" 1, "x 1" 2, "x 2" 3, "x 10" 4) nomirror
set ylabel "Mean final cost"
set xlabel "Mutation rate (1 / genome length)"

set key nobox  top left
load 'parula.pal'

plot\
   'mutrates_hard.dat'  u 0:2:3 w yerr lt 1 lw 3         t  '',\
   ''                   u 0:4:5 w yerr lt 2 lw 3         t  '',\
   ''                   u 0:6:7 w yerr lt 3 lw 3         t  '',\
   ''                   u 0:8:9 w yerr lt 4 lw 3         t  '',\
   ''                   u 0:2   w l    lt 1 lw 3         t  'B',\
   ''                   u 0:4   w l    lt 2 lw 3 dt '--' t  'G',\
   ''                   u 0:6   w l    lt 3 lw 3 dt '._' t  'CB',\
   ''                   u 0:8   w l    lt 4 lw 3 dt '__' t  'CG'
   
set o
set terminal wxt
