function Y = simhop(w,epoch,Yo);

% function Y = simhop(w,epoch,Yo);
%
% Simulates the Hopfield model defined the newhop2 routine.
% epoch is the number of times to update the nodes. Yo is
% the initial configuration.
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se


Y = Yo;

[N,M] = size(w);
for i=1:epoch
  [aa,idx] = sort(rand(1,N));
  for j=1:N
    tmp = dot(w(idx(j),:),Y);
    newY = sign(tmp);
    if newY ~= 0
      Y(idx(j)) = newY;
    end
  end
end


