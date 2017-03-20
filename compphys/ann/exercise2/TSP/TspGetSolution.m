function [v,status]=TspGetSolution(w,Loc,size)

% Here we extract the solution found by the Elastic snake method
sdist=dist(Loc,w');
[smin,idx]=min(sdist');
tmp=[[1:size]',idx'];
idx2=sortrows(tmp,2);

v=zeros(size);
for i=1:size,
  v(idx2(i,1),i)=1;
end

% Check the solution
tmp=sum(v);
if isempty(find(tmp>1)) == 1
  status=1;
else
  status=-1;
end

