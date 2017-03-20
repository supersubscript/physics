function [P,T] = loadsyn1(N)

% function [P,T] = loadsyn1(N)
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

% The positives
P(1,1:N1)=0.5+randn(1,N1);
P(2,1:N1)=0.5+randn(1,N1);

% The negatives
P(1,N1+1:N)=-0.5+randn(1,N-N1);
P(2,N1+1:N)=-0.5+randn(1,N-N1);

% Normalize
[P, settings] = mapstd(P); 

% The targets
T(1,1:N1)=ones(1,N1);
T(1,N1+1:N)=zeros(1,N-N1);
