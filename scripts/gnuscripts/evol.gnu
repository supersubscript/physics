reset
#set t pngcairo color enhanced font 'Verdana, 10' size 700,400
load 'parula.pal'
set t postscript eps enhanced size 10cm,6.5cm color colortext
compl="LINEAR"
set o "~/text/".compl."_evol.eps"

set border 3
set yrange [0:50]
set xrange [0:100000]
set xtics 25000 nomirror
set ytics nomirror
set xlabel "Generation"
set ylabel "Cost"

set key nobox
load 'parula.pal'

file="stat.dat"
plot\
   'BINARY_'.compl.'_0.01/'.file               using 1:2    w l      lt 1 lw 2.0           t 'Binary',\
   '' using 1:2:($3/sqrt(100)) every 2000::200              w yerr   lt 1 lw 2.0           t '',\
   'GRAY_'.compl.'_0.01/'.file                 using 1:2    w l      lt 2 lw 2.0   dt '--' t 'Gray',\
   '' using 1:2:($3/sqrt(1e2)) every 2000::400              w yerr   lt 2 lw 2.0           t '',\
   'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file   using 1:2    w l      lt 3 lw 2.0   dt '._' t 'Consensus Binary',\
   '' using 1:2:($3/sqrt(1e2)) every 2000::600              w yerr   lt 3 lw 2.0           t '',\
   'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file     using 1:2    w l      lt 4 lw 2.0   dt '__' t 'Consensus Gray',\
   '' using 1:2:($3/sqrt(1e2)) every 2000::800              w yerr   lt 4 lw 2.0           t ''

set o
set terminal wxt
