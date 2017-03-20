function [P,T] = loadliverval
%
% function [P,T] = loadliverval
%
% Loads liver data onto P (inputs) and T (targets).
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

load liverVal.dat
P = liverVal(:,[1 2 3 4 5 6 7 8 9 10])';
T = liverVal(:,11)';
