
% Clear all thing before
clear all;
close all;

% Initialize the random number generator

sel = [1 3 7 9];
  
% Load training and validation data
[Pt,Tt] = loadmg17(1, sel);
[Pv,Tv] = loadmg17(2, sel); 

% Ask for training method
disp(sprintf('Choose method:'));
disp(sprintf('Gradient descent backprop.                  = 1'));
disp(sprintf('Gradient descent backprop. with momentum    = 2'));
disp(sprintf('Gradient descent backprop. with adaptive LR = 3'));
disp(sprintf('Gradient descent backprop. with [2] and [3] = 4'));
disp(sprintf('Polak-Ribiére conjugate gradient            = 5'));
disp(sprintf('Scaled conjugate gradient                   = 6'));
disp(sprintf('BFGS quasi-Newton method                    = 7'));
disp(sprintf('Levenberg-Marquardt method                  = 8'));
disp(sprintf('RPROP - Resilient backpropagation           = 9'));
tmp =  input('                        method? (default 4) = ');
if isempty(tmp) == 1
  method='traingdx';
elseif tmp == 1
  method='traingd';
elseif tmp == 2
  method='traingdm';
elseif tmp == 3
  method='traigda';
elseif tmp == 4
  method='traingdx';
elseif tmp == 5
  method='traincgp';
elseif tmp == 6
  method='trainscg';
elseif tmp == 7
  method='trainbfg';
elseif tmp == 8
  method='trainlm';
elseif tmp == 9
  method='trainrp';
end
  errFcn='mse';
  epoch=300;

fprintf('nodes\t totv\t specv\t sensv\t bacv\n');
for i=21:21
nodes = i;
tmp = 2;
  rand('state',tmp);
  randn('state',tmp);
% Create a MLP using feedforwardnet
net = feedforwardnet(nodes, method);

% Set the error function
net.performFcn = errFcn;

% Set the correct activation functions
net.layers{1}.transferFcn = 'tansig';
net.layers{2}.transferFcn = 'purelin';

% Other options 
net.outputs{2}.processFcns = {};    % To avoid rescaling of outputs
net.trainParam.showCommandLine = 0; % To show the error on the commandline
net.trainParam.epochs = epoch;      % Number of epochs
net.trainParam.max_fail = epoch+1;  % Avoid early stopping
net.trainParam.min_grad = 0;       % Avoid stopping because of small gradient
%net.trainParam.lr = .96

% This is to make train show the validation errors during training
net.divideFcn = 'divideind';                     % Divide according to an index
net.divideParam.trainInd = 1:300;            % The first Ndata will
                                                 % be training
net.divideParam.valInd = 301:400;   % The next Nval (1000) will be validation

%Train the network
Pc = [Pt Pv];          % This is both the training and the validation set
Tc = [Tt(1,:) Tv(1,:)];          % They are divided during the training according
                       % to the indexes above
net = train(net,Pc,Tc); 
% Plot the results
%plotres(net,Pt,Tt,'Training data');
%plotres(net,Pv,Tv,'Validation data');
%boundary(net,Pt,Tt,500,0.5,0.01);

% Perform some analysis on the result
Y = sim(net,Pt);
%[spec, sens, tot, None, Nzero, miss, bac] = stat(Y, Tt);
% The validation data
Yv = sim(net,Pv);
%[specv, sensv, totv, Nonev, Nzerov, missv, bacv] = stat(Yv, Tv);

err = evalmg17(net,1, sel);

% fprintf('%.0f%',nodes);
% fprintf('\t');
% fprintf('%.2f%',totv);
% fprintf('\t');
% fprintf('%.2f%',specv);
% fprintf('\t');
% fprintf('%.2f%',sensv);
% fprintf('\t');
% fprintf('%.2f%',bacv);
% fprintf('\t\n')
end

