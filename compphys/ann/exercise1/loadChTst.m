function P = loadChTst
%
% function P = loadChTst
%
% Loads Challenge test data onto P (inputs).
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

load challenge_tst.dat
P = challenge_tst(:,1:8)';
