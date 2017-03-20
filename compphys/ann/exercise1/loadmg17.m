function [P,T] = loadmg17(iset,sel)

% function [P,T] = loadmg17(iset)
% function [P,T] = loadmg17(iset,sel)
%
% Returns the Mackey-Glass (17) data. 
%
% If iset=1, then t = 1-300 are returned (training).
% If iset=2, then t = 301-400 are returned (test).
%
% The optional sel parameter determines the timelags to use. Example, if sel
% = [1 2 6 10] then the returned P matrix will have 4 rows, corresponding to
% x at t-1, t-2, t-6 and t-10. If you don't provide a sel parameter P
% will contain 10 rows, corresponding to t-1,...,t-10.
%
% The returned T vector has two rows. The first is x(n) and the second n.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Load the datafile
load mg17.dat

if nargin < 1
  error('You have to give iset value');
end
if nargin == 2
  [n,m] = size(sel);
  if m > 10
    error('You only specify a maximum of 10 timelags');
  end
  if max(sel) > 10
    error('The largest timelag you can specify is 10');
  end
else
  sel = [1 2 3 4 5 6 7 8 9 10];
end

% Set up the offsets
if iset == 1
  lowidx=11;
  highidx=300;
elseif iset == 2
  lowidx=301;
  highidx=400;
else
  error('Undefined iset value');
end

% Load the target vector
T(1,:) = mg17(lowidx:highidx,1)';
T(2,:) = linspace(lowidx,highidx,(highidx-lowidx+1));

% The time lags
for i=1:10,
  Pt(i,:) = mg17(lowidx-i:highidx-i,1)';
end

% Returns the specified timelags
P = Pt(sel,:);
