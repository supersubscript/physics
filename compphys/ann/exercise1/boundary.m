function boundary(net, P, T, Nsize, cut, eps);
%
% function boundary(net, P, T, Nsize, cut, eps);
%
% This function will scan the 2-dimensional input space
% where the inputs P are defined. If the output from the 
% network is within a specified range a point is plotted
% at the input coordinates.
% 
% net = a trained network
% P = inputs that was used when training the network
% T = targets for P (used to plot the two classes)
% Nsize = the size of the grid that will be scanned.
% cut, eps = if the output o from the network satisfies
%            abs(o-cut) < eps 
%            then a point will be plotted for that gridpoint
%
% Example: 
% You have trained a network (net) to separate two classes,
% defined by P and T. To plot the decision line (the line that
% separates the two classes) try
%
% boundary(net, P, T, 300, 0.5, 0.05);
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Check that P is 2-dimensional
tmp = size(P);
if tmp(1) ~= 2
  error('Input data is not 2-dimensional');
end

% Make new plot
figborder = figure;
plotpv(P,T);
hold on;
grid on;

% Make some useful variables
gs = 1.1 * minmax(P);
input = zeros(2,Nsize);
dx = (gs(1,2) - gs(1,1))/(Nsize-1);
dy = (gs(2,2) - gs(2,1))/(Nsize-1);
input(1,:) = [gs(1,1):dx:gs(1,2)];
yy = gs(2,1) - dy;

% Scan the grid, line by line
for i=1:Nsize,
  yy = yy + dy;
  input(2,:) = yy * ones(1,Nsize);
  
  % Simulate the network with grid line at yy
  out = sim(net,input);

  % Find output values close to cut
  idx = find(abs(out-cut) < eps);
  plx = input(1,idx);
  ply = input(2,idx);
  plot(plx,ply,'r.','MarkerSize', 4.5);
  
  % Update the plot every 50 lines
  if mod(i,50) == 0 
    drawnow;
  end
end
