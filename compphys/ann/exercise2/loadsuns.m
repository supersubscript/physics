function [P,T,Yp,Yt] = loadsuns(iset)

% function [P,T,Yp,Yt] = loadsuns(iset)
%
% Returns the annual sunspot number. 
%
% If iset=1, then the years 1700-1930 are returned (training set). If
% iset=2, then the years 1931-2014 are returned (test set).
%
% T is the sunspot number series and Y contains the years 
% for the numbers. 
%
% Dec 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Load the datafile
load sunspot.dat

if nargin < 1
  error('You have to give iset value');
end

% Set up the offsets
if iset == 1
  lowidx=2;
  highidx=231;
elseif iset == 2
  lowidx=232;
  highidx=313;
else
  error('Undefined iset value');
end

% Load the target vector
tmpT = sunspot(lowidx:highidx,2)';

% Make input and output
P = tmpT(1:end-1);
T = tmpT(2:end);

% The year vector
tmpY = sunspot(lowidx:highidx,1)';

% Make input and output
Yp = tmpY(1:end-1);
Yt = tmpY(2:end);
