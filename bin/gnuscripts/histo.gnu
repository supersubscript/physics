#set t pngcairo color enhanced font 'Verdana, 10' size 700,350
reset
#### Terminal settings and output
set t postscript eps enhanced size 15cm,6.5cm color colortext 
compl="LADDER"
set o "~/text/".compl.".eps"

#### Histogram settings
binwidth=0.1  
dp = 40000.
bin(x,width)=width*floor(x/width)

#### Plot settings
set key nobox outside  top left horizontal maxrows 1
set xlabel "Generation"
set ylabel "Quotient"
set border 3
unset margins
set yrange [0:2.]
set xrange [0:3.3]
set xtics ("0" .2, "10" .7, "100" 1.2, "1000" 1.8, "10000" 2.4, "100000" 3.0 ) nomirror
set ytics norangelimited ("0" 0, "0.5" .5, "1.0" 1, "1.5" 1.5, "2.0" 2) nomirror
load 'parula.pal'

# plot specifics
filter = 'grep -vi -- "-\?\(NaN\|infinity\|1.00\)"'
f(x) = 1
load 'parula.pal'

file="0.dat"
set table $kd1
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd2
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd3
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd4
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    

file="10.dat"
set table $kd5
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd6
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd7
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd8
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    

file="100.dat"
set table $kd9
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd10
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd11
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd12
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    

file="1000.dat"
set table $kd13
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd14
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd15
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd16
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    

file="10000.dat"
set table $kd17
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd18
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd19
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd20
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    

file="99990.dat"
set table $kd21
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd22
plot '< '.filter.' GRAY_'.compl.'_0.01/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd23
plot '< '.filter.' CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
set table $kd24
plot '< '.filter.' CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file    using (bin($1,binwidth) + binwidth/2):(1.0/dp) smooth freq w l    
unset table

#set o compl.".png"

load 'parula.pal'



plot\
      f(x) lt -1 dashtype ".." t '',\
      $kd1  u (+$2 +  .2):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t 'Binary',\
      ''    u (-$2 +  .2):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd2  u (+$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t 'Gray',\
      ''    u (-$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd3  u (+$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t 'Consensus Binary',\
      ''    u (-$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd4  u (+$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t 'Consensus Gray',\
      ''    u (-$2 +  .2):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      \
      $kd5  u (+$2 +  .7):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      ''    u (-$2 +  .7):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd6  u (+$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      ''    u (-$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd7  u (+$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      ''    u (-$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd8  u (+$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      ''    u (-$2 +  .7):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      \
      $kd9  u (+$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      ''    u (-$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd10 u (+$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      ''    u (-$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd11 u (+$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      ''    u (-$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd12 u (+$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      ''    u (-$2 + 1.2):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      \
      $kd13 u (+$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      ''    u (-$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd14 u (+$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      ''    u (-$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd15 u (+$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      ''    u (-$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd16 u (+$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      ''    u (-$2 + 1.8):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      \
      $kd17 u (+$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      ''    u (-$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd18 u (+$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      ''    u (-$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd19 u (+$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      ''    u (-$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd20 u (+$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      ''    u (-$2 + 2.4):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      \
      $kd21 u (+$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      ''    u (-$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l                  lt 1 lw 2.0 t '',\
      $kd22 u (+$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      ''    u (-$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "--"    lt 2 lw 2.0 t '',\
      $kd23 u (+$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      ''    u (-$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "._"    lt 3 lw 2.0 t '',\
      $kd24 u (+$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t '',\
      ''    u (-$2 + 3.0):($1/(stringcolumn(3) eq "i")) w l dashtype "__"    lt 4 lw 2.0 t ''

set o
