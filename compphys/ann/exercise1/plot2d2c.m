function plot2d2c(P,T)

% function plot2d2c(P,T)
%
% Plots the classes of a 2-dimensional classification problem (with 2
% classes). The data are stored in P (inputs) and T (targets).
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Check that input data are consistent
[Pd,Pn] = size(P);
if Pd ~= 2
  error('Input data must be two-dimensional');
end
[Td,Tn] = size(T);
if Tn ~= Pn
  error('The number of target values must match the number of input patterns')
end

% Make the plot
plotfig = figure;
title('Class 0 = red, Class 1 = blue');
hold on;
I1=T(1,:)>0.5;
I2=T(1,:)<0.5;
plot(P(1,I1),P(2,I1),'b+',P(1,I2),P(2,I2),'ro')
