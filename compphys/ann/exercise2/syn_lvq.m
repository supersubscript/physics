clear all;
close all;

% Default parameters
def_nodes = 7;
def_lr = 0.008;
def_Ndata = 100;
def_epoch = 50;

% Define the problem
disp(sprintf('Choose problem:'));   
disp(sprintf('Synthetic data I    = 1'));
disp(sprintf('Synthetic data II   = 2'));
disp(sprintf('Synthetic data III  = 3'));
tmp=input(' problem? (default 3): ');

if isempty(tmp) == 1
  problem = 3;
else
  problem=tmp;
end

% How many data points
tmp = input(sprintf('How many data? [%d] ', def_Ndata));

if tmp > 0
  Ndata = tmp;
else
  Ndata = def_Ndata;
end



% Load training and validation data
if problem == 1
  % Load artificial data I
  [P,Tt] = loadsyn1(Ndata);
  [Pv,Ttv] = loadsyn1(Ndata);
elseif problem == 2
  % Load artificial data II
  [P,Tt] = loadsyn2(Ndata);
  [Pv,Ttv] = loadsyn2(Ndata);
elseif problem == 3
  % Load artificial data II
  [P,Tt] = loadsyn3(Ndata);
  [Pv,Ttv] = loadsyn3(Ndata);
else
  error('Use 1, 2 or 3 to select the problem type');
end

% We should represent the classes as 2 outputs
[n,m] = size(P);
T = zeros(2,m);
T(1, find(Tt==1)) = 1;
T(2, find(Tt==0)) = 1;

[n,m] = size(Pv);
Tv = zeros(2,m);
Tv(1, find(Ttv==1)) = 1;
Tv(2, find(Ttv==0)) = 1;

% How many output neurons
tmp = input(sprintf('Number of output nodes? [%d] ',def_nodes));
if tmp > 0
  nodes=tmp;
else
  nodes=def_nodes;
end

% Perhaps you want to change the learning rate
tmp = input(sprintf('The LVQ learning rate [%f] ', def_lr));
if tmp > 0
  lr = tmp;
else
  lr = def_lr;
end

% How many epochs
tmp = input(sprintf('Number of epochs? [%d] ', def_epoch));
if tmp > 0
  epoch = tmp;
else
  epoch = def_epoch;
end

for a=1:10
  rand('state',sum(100*clock));
  randn('state',sum(100*clock));
  
    %if a>1
    %    def_lr = def_lr + 0.001;
    %end

% Create a LVQ network
net = lvqnet(nodes, lr, 'learnlv1');

% Change parameters
net.trainParam.epochs = epoch;

% Train the LVQ classifier 
net = train(net,P,T);

% Plot the results
lvqres(net,P,T,'Training',1,1);
lvqres(net,Pv,Tv,'Validation',0,2);

% Perform some analysis on the result
Y = sim(net,P);
Y01 = vec2ind(Y)-1;
T01 = vec2ind(T)-1;
[spec, sens, tot, None, Nzero, miss, bac] = stat(Y01, T01);

% disp(sprintf('\n'));
% disp(sprintf('Results for the training:'));
% disp(sprintf('Total number of data: %d (%d ones and %d zeros)', ...
% 	     length(T01), None, Nzero)); 
% disp(sprintf('Number of misses: %d (%5.2f%% performance)', miss, tot));
% disp(sprintf('Specificity: %5.2f%% (Success for class 0)', spec));
% disp(sprintf('Sensitivity: %5.2f%% (Success for class 1)', sens));
% disp(sprintf('Average Sens and Spec: %5.2f%%', bac));

% The validation data
Yv = sim(net,Pv);
Y01 = vec2ind(Yv)-1;
T01 = vec2ind(Tv)-1;
[specv, sensv, totv, Nonev, Nzerov, missv, bacv] = stat(Y01, T01);


%boundaryLVQ(net, P, T, Ndata);
hold on; 

%disp(sprintf('\nResults for the validation:'));
%disp(sprintf('Total number of data: %d (%d ones and %d zeros)', ...
	     %length(T01), Nonev, Nzerov)); 
disp(sprintf('Number of misses: %d %5.4f (%5.2f%% performance)', ...
	     missv, def_lr, totv));
%disp(sprintf('Specificity: %5.2f%% (Success for class 0)', specv));
%disp(sprintf('Sensitivity: %5.2f%% (Success for class 1)', sensv));
%disp(sprintf('Average Sens and Spec: %5.2f%%', bacv));
end