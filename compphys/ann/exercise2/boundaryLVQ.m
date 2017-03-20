function boundaryLVQ(net, P, T, Nsize);
%
% function boundaryLVQ(net, P, T, Nsize);
%
% This function will scan the 2-dimensional input space
% where the inputs P are defined. It will mark points where
% the LVQ network change the class prediction.
% 
% net = a trained network
% P = inputs that was used when training the network
% T = targets for P (used to plot the two classes)
% Nsize = the size of the grid that will be scanned.
%
% Dec 2015, Mattias Ohlsson
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

% Make comparison
inputO = input;
inputO(2,:) = (yy+dy) * ones(1,Nsize);
outOT = sim(net,inputO);
outO = vec2ind(outOT) - 1;

% Scan the grid, line by line
for i=1:Nsize,
  yy = yy + dy;
  input(2,:) = yy * ones(1,Nsize);
  
  % Simulate the network with grid line at yy
  outT = sim(net,input);
  out = vec2ind(outT) - 1;
  
  % Find output values close to cut
  idx = find(abs(out-outO) > 0.5);
  plx = input(1,idx);
  ply = input(2,idx);
  plot(plx,ply,'r.','MarkerSize', 4.5);
  
  % update old
  outO = out;
  
  % Update the plot every 50 lines
  if mod(i,50) == 0 
    drawnow;
  end
end

% Plot the weight vectors (i.e. the cluster locations)
w = net.iw{1,1};

% Find cluster class
wP = sim(net,w');
wP = vec2ind(wP);
idx1 = find(wP==2);
idx0 = find(wP==1);
plot(w(idx0,1),w(idx0,2),'gs','markersize',10,'MarkerEdgeColor','k','MarkerFaceColor','g');
plot(w(idx1,1),w(idx1,2),'ys','markersize',10,'MarkerEdgeColor','k','MarkerFaceColor','y');

title('Green="o" clusters : Yellow="+" clusters');
