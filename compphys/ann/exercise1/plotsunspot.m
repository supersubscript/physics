function plotsunspot

% function plotsunspot
%
% Plots the sunspot data. The data is divided into a 
% training- and a test set.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Load the data
[P,T] = loadsunspot(1);
[P,Tt] = loadsunspot(2);

% Make the plot
plotsun = figure;
subplot(2,1,1);
title('Sunspot data: Training 1700 - 1930');
hold on;
plot(T(2,:),T(1,:),'r-');
 
subplot(2,1,2);
title('Sunspot data: Test 1931 - 2014');
hold on;
plot(Tt(2,:),Tt(1,:),'r-');
