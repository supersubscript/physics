function [P,T] = loadChTrn
%
% function [P,T] = loadChTrn
%
% Loads Challenge training data onto P (inputs) and T (targets).
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

load challenge_trn.dat
P = challenge_trn(:,1:8)';
T = challenge_trn(:,9)';
