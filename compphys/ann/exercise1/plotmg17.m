function plotmg17

% function plotmg17
%
% Plots the Mackey Glass data. 
% The data is divided into a training- and a test set.
%
% Nov 2017, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Load the data
[P,T] = loadmg17(1);
[P,Tt] = loadmg17(2);

% Make the plot
plotsun = figure;
subplot(2,1,1);
title('Mackey Glass data: Training 1-300');
hold on;
plot(T(2,:),T(1,:),'r-');
 
subplot(2,1,2);
title('Mackey Glass data: Test 301-400');
hold on;
plot(Tt(2,:),Tt(1,:),'r-');
