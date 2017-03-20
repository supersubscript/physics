reset
#set t pngcairo color enhanced font 'Verdana, 10' size 700,400
set macros
load 'parula.pal'
set t postscript eps enhanced size 8,8 font 'Helvetica, 24' color colortext
compl="LINEAR"
set o "~/text/"."multimutrates.eps"

set border 3
set yrange [0:]
set xrange [0:4]
set ytics 100 nomirror
set xtics ("x 1/10" 0, "x 1/2" 1, "x 1" 2, "x 2" 3, "x 10" 4) nomirror
set ylabel "Mean final cost"
set xlabel "Mutation rate (1 / genome length)"
unset xtics
set grid y
set key nobox
unset key 
load 'parula.pal'
set multiplot layout 3,2
currentplot = 1

set label 1 'A' at graph 0.95,0.95 font '{/Bold:}, 24' 
unset xlabel

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

unset label
set size .41,.334
set label 2 'B' at graph 0.95,0.95 font '{/Bold:}, 24' 
unset xlabel
unset ylabel
set format y ""
file="mutrates_lad.dat"
plot\
   file u 0:2:3 w yerr lt 1 lw 3         t  '',\
   ''             u 0:4:5 w yerr lt 2 lw 3         t  '',\
   ''             u 0:6:7 w yerr lt 3 lw 3         t  '',\
   ''             u 0:8:9 w yerr lt 4 lw 3         t  '',\
   ''             u 0:2   w l    lt 1 lw 3         t  '',\
   ''             u 0:4   w l    lt 2 lw 3 dt '--' t  '',\
   ''             u 0:6   w l    lt 3 lw 3 dt '._' t  '',\
   ''             u 0:8   w l    lt 4 lw 3 dt '__' t  ''


unset label
set label 3 'C' at graph 0.95,0.95 font '{/Bold:}, 24' 
set ylabel "Mean final cost"
set xlabel "Mutation rate (1 / genome length)"
set ytics nomirror
set format y "%.0f"
set xtics ("x 1/10" 0, "x 1/2" 1, "x 1" 2, "x 2" 3, "x 10" 4) nomirror
set origin 0.01,.32
set size .49,.364

file="mutrates_sin.dat"
plot\
   file u 0:2:3 w yerr lt 1 lw 3         t  '',\
   ''             u 0:4:5 w yerr lt 2 lw 3         t  '',\
   ''             u 0:6:7 w yerr lt 3 lw 3         t  '',\
   ''             u 0:8:9 w yerr lt 4 lw 3         t  '',\
   ''             u 0:2   w l    lt 1 lw 3         t  '',\
   ''             u 0:4   w l    lt 2 lw 3 dt '--' t  '',\
   ''             u 0:6   w l    lt 3 lw 3 dt '._' t  '',\
   ''             u 0:8   w l    lt 4 lw 3 dt '__' t  ''


unset label
set label 4 'D' at graph 0.95,0.95 font '{/Bold:}, 24' 
set size .41,.364
set origin 0.5,.32
compl="HARD_SINUSOIDAL"
unset ylabel
set format y ""
set ytics 100
set yrange [0:400]
file="mutrates_hard.dat"
plot\
   file u 0:2:3 w yerr lt 1 lw 3         t  '',\
   ''             u 0:4:5 w yerr lt 2 lw 3         t  '',\
   ''             u 0:6:7 w yerr lt 3 lw 3         t  '',\
   ''             u 0:8:9 w yerr lt 4 lw 3         t  '',\
   ''             u 0:2   w l    lt 1 lw 3         t  '',\
   ''             u 0:4   w l    lt 2 lw 3 dt '--' t  '',\
   ''             u 0:6   w l    lt 3 lw 3 dt '._' t  '',\
   ''             u 0:8   w l    lt 4 lw 3 dt '__' t  ''


set origin .27,.13
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
