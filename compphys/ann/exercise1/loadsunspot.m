function [P,T] = loadsunspot(iset,sel)

% function [P,T] = loadsunspot(iset)
% function [P,T] = loadsunspot(iset,sel)
%
% Returns the annual sunspot number. 
%
% If iset=1, then the years 1700-1930 are returned (training set). If
% iset=2, then the years 1931-2014 are returned (test set).
%
% The optional sel parameter determines the timelags to use. Example, if sel
% = [1 2 6 12] then the returned P matrix will have 4 rows, corresponding to
% the sunspot number in year t-1, t-2, t-6 and t-12. If you don't provide a
% sel parameter P will contain 12 rows, corresponding to t-1,...,t-12.  The
% returned T vector has two rows. The first is the sunspot number at year t
% and the second row is the year t.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Load the datafile
load sunspot.dat

if nargin < 1
  error('You have to give iset value');
end
if nargin == 2
  [n,m] = size(sel);
  if m > 12
    error('You only specify a maximum of 12 timelags');
  end
  if max(sel) > 12
    error('The largest timelag you can specify is 12');
  end
else
  sel = [1 2 3 4 5 6 7 8 9 10 11 12];
end

% Set up the offsets
if iset == 1
  lowidx=13;
  highidx=231;
elseif iset == 2
  lowidx=232;
  highidx=315;
else
  error('Undefined iset value');
end

% Load the target vector
T(1,:) = sunspot(lowidx:highidx,2)';
T(2,:) = sunspot(lowidx:highidx,1)';

% The time lags
for i=1:12,
  Pt(i,:) = sunspot(lowidx-i:highidx-i,2)';
end

% Returns the specified timelags
P = Pt(sel,:);
