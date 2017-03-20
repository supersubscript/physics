reset
#set t pngcairo color enhanced font 'Verdana, 10' size 700,400
set macros
load 'parula.pal'
set t postscript eps enhanced size 4,6 font 'Helvetica, 24' color colortext
compl="LADDER"
set o "~/bach_presentation/figures/"."pres_mutrates_a.eps"

set border 3
set yrange [0:]
set xrange [0:4]
set ytics 100 nomirror
set xtics ("x 1/10" 0, "x 1/2" 1, "x 1" 2, "x 2" 3, "x 10" 4) nomirror
set ylabel "Mean final cost"
set xlabel "Mutation rate (1 / genome length)"
set grid y
set key nobox
unset key 
load 'parula.pal'
set multiplot layout 2,1
currentplot = 1

set label 1 'A' at graph 0.95,0.95 font '{/Bold:}, 24' 

file="mutrates.dat"
plot\
   file u 0:2:3 w yerr lt 1 lw 3         t  '',\
   ''             u 0:4:5 w yerr lt 2 lw 3         t  '',\
   ''             u 0:6:7 w yerr lt 3 lw 3         t  '',\
   ''             u 0:8:9 w yerr lt 4 lw 3         t  '',\
   ''             u 0:2   w l    lt 1 lw 3         t  '',\
   ''             u 0:4   w l    lt 2 lw 3 dt '--' t  '',\
   ''             u 0:6   w l    lt 3 lw 3 dt '._' t  '',\
   ''             u 0:8   w l    lt 4 lw 3 dt '__' t  ''

set origin 0.05,.22
set key center center horizontal maxrows 1
set border 0
unset label
unset tics
unset xlabel
unset ylabel
set yrange[-1:1]   

plot 2 w l lt 1 lw 3         t 'B', \
     2 w l lt 2 lw 3 dt "--" t 'G', \
     2 w l lt 3 lw 3 dt "._" t 'CB', \
     2 w l lt 4 lw 3 dt "__" t 'CG'

unset multiplot
set o
set terminal wxt
