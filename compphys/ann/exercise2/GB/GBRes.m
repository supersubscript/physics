function cut = GBRes(T,s,alpha,ndim,TotUp,method,cpu,aux1,aux2,aux3,aux4)

if method==1 
  disp(sprintf('\nResults from the PURE Hopfield net:'));
  disp(sprintf('-----------------------------------'));
elseif method==2
  disp(sprintf('\nResults from the STOCHASTIC Hopfield net:'));
  disp(sprintf('-----------------------------------------'));
elseif method==3
  disp(sprintf('\nResults from the MEAN FIELD Hopfield net:'));
  disp(sprintf('-----------------------------------------'));
end
  
[Ep,Ec]=GBGetEnergy(T,s,alpha);
cutsize(1)=length(find((T.*(s*s'))<0))/2;
disp(sprintf('Energy = %f + %f = %f', Ep, Ec, Ep+Ec));
disp(sprintf('Cutsize = %d',cutsize(1)));
noc1=length(find(s>0));
noc2=length(find(s<0));
disp(sprintf('Number of nodes in the two groups: %d %d', noc1, noc2));
disp(sprintf('Number of updates = %d', TotUp));
if method==2
  disp(sprintf('Number of negative v -> s= +1: %d', aux1));
  disp(sprintf('Number of negative v -> s= -1: %d', aux2));
  disp(sprintf('Number of positive v -> s= -1: %d', aux3));
  disp(sprintf('Number of Positive v -> v= +1: %d', aux4));
end
disp(sprintf('Cpu time = %f', cpu));
cut = cutsize(1);

