function [P,T] = loadsyn3(N)

% function [P,T] = loadsyn3(N)
%
% Generates N 2-dimensional data points sampled from two overlapping normal
% distributions. The data are stored in P and the class-membership in T.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

if nargin < 1
  error('You must give the number of points as input argument')
end
if nargout < 2 
  error('You must provide vectors/matrices P and T for output')
end

N1 = round(N/2);
Rpos = 0.6;
Rneg = 0.9;

% The positives
tmpang = 2.0 * pi * rand(1,N1);
tmpr = Rpos * randn(1,N1);
P(1,1:N1)=tmpr .* cos(tmpang);
P(2,1:N1)=tmpr .* sin(tmpang);

% The negatives
tmpang = 2.0 * pi * rand(1,N1);
tmpr = rand(1,N1) + Rneg;
P(1,N1+1:N)=tmpr .* cos(tmpang);
P(2,N1+1:N)=tmpr .* sin(tmpang);

% Normalize
[P, settings] = mapstd(P);

% The targets
T(1,1:N1)=ones(1,N1);
T(1,N1+1:N)=zeros(1,N-N1);
