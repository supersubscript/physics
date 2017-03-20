function [Ep,Ec] = GBGetEnergy(T,s,alpha)

% Calculate the propotional energy
Ep=-s'*(T*s);

% Calculate the contraint energy
Ec=alpha*sum(s)^2;




