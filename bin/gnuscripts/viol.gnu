set auto x
set xtics 0,50,500
unset ytics
set border 3
set key
a(x) = 1-.03
b(x) = 1-.02
c(x) = 1-.01
d(x) = 1
e(x) = 1+.01
f(x) = 1+.02
g(x) = 1+.03

compl="LINEAR"
bw = .0017
dp = 40000.

# figure distance, group distance
fd=0.0
gd=200
filter = 'grep -vi -- "-\?\(NaN\|infinity\)"'

set xrange [0:2]
file="99990.dat"
set table $kd1
plot '< '.filter.' BINARY_'.compl.'_0.01/'.file     using 1:(1./dp)  smooth freq  with boxes
#set table $kd2
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd3
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd4
#plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  

#file="10.dat"
#set table $kd5
#plot 'BINARY_'.compl.'_0.01/'.file                 using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd6
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd7
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd8
#plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  
#
#file="100.dat"
#set table $kd9
#plot 'BINARY_'.compl.'_0.01/'.file                 using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd10
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd11
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd12
#plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  
#
#file="1000.dat"
#set table $kd13
#plot 'BINARY_'.compl.'_0.01/'.file                 using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd14
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd15
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd16
#plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  
#
#file="10000.dat"
#set table $kd17
#plot 'BINARY_'.compl.'_0.01/'.file                 using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd18
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd19
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd20
#0plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  
#
#file="99990.dat"
#set table $kd21
#plot 'BINARY_'.compl.'_0.01/'.file                 using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd22
#plot 'GRAY_'.compl.'_0.01/'.file                   using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd23
#plot 'CONSENSUS_BINARY_'.compl.'_2.0E-4/'.file     using 1:($2/dp)  smooth kdensity bandwidth bw  
#set table $kd24
#plot 'CONSENSUS_GRAY_'.compl.'_2.0E-4/'.file       using 1:($2/dp)  smooth kdensity bandwidth bw  
unset table
unset key

set border 2
unset margins
unset xtics
set ytics nomirror rangelimited
set key

set yrange [0:2]
set xrange [:]
plot\
       f(x) lt -1 dashtype "..",\
       $kd1 using (1 + 0*gd + 0*fd + $2):1 w l  lc rgb 'blue' t 'BIN',\
       ''   using (1 + 0*gd + 0*fd - $2):1 w l  lc rgb 'blue' 
#      $kd2 using (1 + 0*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red' t 'GRAY',    \
#      ''   using (1 + 0*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd3 using (1 + 0*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green' t 'C_B', \
#      ''   using (1 + 0*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd4 using (1 + 0*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange' t 'C_G', \
#      ''   using (1 + 0*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange', \
#      \
#      $kd5 using (1 + 1*gd + 0*fd + $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      ''   using (1 + 1*gd + 0*fd - $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      $kd6 using (1 + 1*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red',    \
#      ''   using (1 + 1*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd7 using (1 + 1*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green',  \
#      ''   using (1 + 1*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd8 using (1 + 1*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange', \
#      ''   using (1 + 1*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange', \
#      \
#      $kd9 using (1 + 2*gd + 0*fd + $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      ''   using (1 + 2*gd + 0*fd - $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      $kd10 using (1 + 2*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red',    \
#      ''   using (1 + 2*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd11 using (1 + 2*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green',  \
#      ''   using (1 + 2*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd12 using (1 + 2*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange', \
#      ''   using (1 + 2*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange',  \
#      \
#      $kd13 using (1 + 3*gd + 0*fd + $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      ''   using (1 + 3*gd + 0*fd - $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      $kd14 using (1 + 3*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red',    \
#      ''   using (1 + 3*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd15 using (1 + 3*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green',  \
#      ''   using (1 + 3*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd16 using (1 + 3*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange', \
#      ''   using (1 + 3*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange',  \
#      \
#      $kd17 using (1 + 3*gd + 0*fd + $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      ''   using (1 + 3*gd + 0*fd - $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      $kd18 using (1 + 3*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red',    \
#      ''   using (1 + 3*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd19 using (1 + 3*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green',  \
#      ''   using (1 + 3*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd20 using (1 + 3*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange',\
#      ''   using (1 + 3*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange',\
#      \
#      $kd21 using (1 + 4*gd + 0*fd + $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      ''   using (1 + 4*gd + 0*fd - $2/9.):1 w l lw 2 lc rgb 'blue',   \
#      $kd22 using (1 + 4*gd + 1*fd + $2/9.):1 w l lw 2 lc rgb 'red',    \
#      ''   using (1 + 4*gd + 1*fd - $2/9.):1 w l lw 2 lc rgb 'red',    \
#      $kd23 using (1 + 4*gd + 2*fd + $2/9.):1 w l lw 2 lc rgb 'green',  \
#      ''   using (1 + 4*gd + 2*fd - $2/9.):1 w l lw 2 lc rgb 'green',  \
#      $kd24 using (1 + 4*gd + 3*fd + $2/9.):1 w l lw 2 lc rgb 'orange',\
#      ''   using (1 + 4*gd + 3*fd - $2/9.):1 w l lw 2 lc rgb 'orange'
