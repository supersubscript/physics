function w = newhop2(P);

% function w = newhop2(P);
%
% Creates a standard Hopfield model using patterns
% in P.
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

% The size of the pattern matrix
[N,Npatt] = size(P);

% Store the weights according the original Hopfield rule
w = P(:,:)*P(:,:)';
w = w / N;

% Clear diagonal
for i=1:N,
  w(i,i) = 0.0;
end
