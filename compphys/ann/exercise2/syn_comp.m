% This function implements a competitive network for clustering of a
% synthetic data set of 6 Gaussien distributions.

clear all;
close all;

% Default parameters
def_nodes = 6;
def_lr = 0.01;
def_clr = 0.001;
def_ndata = 100;
def_epoch = 50;

def_ntrain = 3; % Number of epochs between showing information and
                % updating plots

% Load the synthetic cluster data
tmp = input(sprintf('How many data? [%d] ', def_ndata));
if tmp > 0
  ndata=tmp;
else
  ndata=def_ndata;
end
[P,T] = loadclust1(ndata);

% Initialize the random number generator
tmp = input('Seed to the random number generator (Default current time): ');
if tmp > 0
  rand('state',tmp);
  randn('state',tmp);
else
  rand('state',sum(100*clock));
  randn('state',sum(100*clock));
end

% How many output neurons
tmp = input(sprintf('Number of output nodes? [%d] ',def_nodes));
if tmp > 0  
  nodes=tmp;
else
  nodes=def_nodes;
end

tmp = input('Use default learningparameters? [y] ','s');
if isemsyn  pty(tmp) == 1
  lr = def_lr;
  clr = def_clr;
elseif tmp == 'n'
  tmp1 = input(sprintf('Learningrate? [%f] ', def_lr));
  if isempty(tmp1) == 1
    lr = def_lr;
  else
    lr = tmp1;
  end
  tmp1 = input(sprintf('Bias learningrate? [%f] ', def_clr));
  if isempty(tmp1) == 1
    clr = def_clr;
  else
    clr = tmp1;
  end
elseif tmp == 'y'
  lr = def_lr;
  clr = def_clr;
end
    
% Hom many epochs
tmp = input(sprintf('Number of epochs? [%d] ', def_epoch));
if tmp > 0
  epoch = tmp;
else
  epoch = def_epoch;
end
noepoch = 0;

% Create a competivite layer (that does the clustering)
net = competlayer(nodes,lr,clr);

% Just init the weights by training 0 epochs
net.trainParam.epochs = 0;
net = train(net,P);

%% PLOTTING PART
% Plot of initial setup
w = net.IW{1};
plot(w(:,1),w(:,2),'b*');
hold on;
plot(P(1,:),P(2,:),'r.');
text = sprintf('Result after %d epochs', noepoch);
title(text);
drawnow;
hold off;
%% END OF PLOTTING PART

disp('Hit a key to continue');
pause;

% Start to train the network
w_old = w;
while noepoch < epoch
  net.trainParam.epochs = def_ntrain;
  noepoch = noepoch + def_ntrain;
  net = train(net,P);
  w = net.IW{1};
  loc_diff = mean(sum((w'-w_old').^2).^0.5);
  disp(sprintf('Total number of epochs      = %d', noepoch));
  disp(sprintf('Mean weight location change = %f\n', mean(loc_diff)));
  w_old = w;
  
  %% PLOTTING PART
  plot(w(:,1),w(:,2),'b*');
  hold on;
  plot(P(1,:),P(2,:),'r.');
  text = sprintf('Result after %d epochs', noepoch);
  title(text);
  drawnow;
  hold off;
  %% END OF PLOTTING PART
  
end

% Some statistics
% Don't forget to put the biases to zero
net.B{1} = zeros(nodes,1);
Y = sim(net,P);
Yc = vec2ind(Y);
for i=1:nodes
  nodata(i) = length(find(Yc == i));
end

disp('Outnode  Number of data (winners) for this outnode');
for i=1:nodes
  disp(sprintf('%d         %d', i, nodata(i)));
end

disp(sprintf('\n'));
disp('Distance matrix for the output nodes (weight vectors)');
w = net.IW{1};
D = dist(w');
disp(D);
