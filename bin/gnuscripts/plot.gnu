#plot for[i=2:101] 'CONSENSUS_BINARY_LADDER_2.0E-4/fitness.dat' u 1:i w l lt 1 t "cons_bin",\
#for[i=2:101] 'CONSENSUS_GRAY_LADDER_2.0E-4/fitness.dat' u 1:i  w l lt 2 t "cons_gray",\
#for[i=2:101] 'BINARY_LADDER_0.01/fitness.dat' u 1:i w l lt 3 t "binar",\
#for[i=2:101] 'GRAY_LADDER_0.01/fitness.dat' u 1:i  w l lt 4 t "gray"
plot for[i=2:101] 'CONSENSUS_BINARY_LADDER_2.0E-4/fitness.dat' u 1:i w l lt 1 noti
