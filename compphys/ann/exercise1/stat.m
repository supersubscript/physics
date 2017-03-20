function [spec, sens, tot, None, Nzero, miss, bac] = stat(Y, T);
%
% function [spec, sens, tot, None, Nzero, miss] = stat(Y, T);
%
% Calculates the results for a single output classification
% problem. Y is the network output and T is the target output.
%
% The results are returned as
% spec = number of class 0 targets that were correctly classified
% sens = number of class 1 targets that were correctly classified
% tot = total performance
% None = number of class 1 in T
% Nzero = number of class 0 in T
% miss = number of missclassified targets
% bac = average sens and spec
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

if( islogical(Y) )
  Y = double(Y);
end

ntarget = length(T);
if ntarget ~= length(Y)
  error('T and Y not of the same lenght');
end

Nzero = max(1,length(find(T<0.5)));
None = max(1,length(find(T>0.5)));

specerr = length(find((T-Y)<-0.5));
spec = 100.0*(Nzero-specerr)/Nzero;

senserr = length(find((T-Y)>0.5));
sens = 100.0*(None-senserr)/None;

miss = sum(abs(round(Y)-T));
tot = 100.0*(ntarget-miss)/ntarget;

bac = (sens + spec) / 2;
