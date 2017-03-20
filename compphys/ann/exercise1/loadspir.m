function [P,T] = loadspir
%
% function [P,T] = loadspir
%
% Loads spiral data onto P (inputs) and T (targets)
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

load spirdata.dat;
P = spirdata(:,1:2)';
T = spirdata(:,3)';
