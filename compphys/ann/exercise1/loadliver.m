function [P,T] = loadliver
%
% function [P,T] = loadliver
%
% Loads liver data onto P (inputs) and T (targets).
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

load liverTrn.dat
P = liverTrn(:,[1 2 3 4 5 6 7 8 9 10])';
T = liverTrn(:,11)';
