function T=GBGetRndProblem(ndim,pedge)

% Here we get a random graph bisection problem

tmp=pedge/ndim;
T=zeros(ndim);
for i=1:ndim,
  for j=i:ndim,
    if rand<tmp
      T(i,j)=1;
      T(j,i)=1;
    end
  end
  T(i,i)=0;
end
