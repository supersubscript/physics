function GBPlot(s,T,ndim,mode)

radius=0.2;
idx=find(s>0.0);
cm(1)=0.75;
cm(2)=0.5;
len=length(idx);
tmp=0:(len-1);
tmp=tmp*(2*pi)/len;
Loc(idx,1)=cm(1)+radius*cos(tmp');
Loc(idx,2)=cm(2)+radius*sin(tmp');
plot(Loc(idx,1),Loc(idx,2),'rO');
hold on;

idx=find(s<0.0);
cm(1)=0.25;
cm(2)=0.5;
len=length(idx);
tmp=0:(len-1);
tmp=tmp*(2*pi)/len;
Loc(idx,1)=cm(1)+radius*cos(tmp');
Loc(idx,2)=cm(2)+radius*sin(tmp');
plot(Loc(idx,1),Loc(idx,2),'b*');

for i=1:ndim,
  for j=i:ndim,
    if T(i,j)==1
      plot(Loc([i j],1),Loc([i j],2),'b-');
    end
  end
end
plot([0.51 0.51],[0.3 0.7],'g-');
plot([0.49 0.49],[0.3 0.7],'g-');
drawnow;

