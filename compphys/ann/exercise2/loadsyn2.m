function [P,T] = loadsyn2(N)

% function [P,T] = loadsyn2(N)
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
Pt(1,1:N1)=10+2*randn(1,N1);
Pt(2,1:N1)=10*randn(1,N1);

% The negatives
Pt(1,N1+1:N)=-10+2*randn(1,N-N1);
Pt(2,N1+1:N)=10*randn(1,N-N1);

% The targets
T(1,1:N1)=ones(1,N1);
T(1,N1+1:N)=zeros(1,N-N1);

% Rotate the patterns so that it looks more interesting
R = [sqrt(2) sqrt(2);
     -sqrt(2) sqrt(2)];
P = R*Pt;

% And normalize them
P(1,:) = P(1,:)/std(P(1,:));
P(2,:) = P(2,:)/std(P(2,:));
