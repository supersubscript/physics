% syn_mlp
%
% This function trains a MLP for the three synthetic data sets.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Clear all thing before
clear all;
close all;


% How many data points

Ndata = 100;
Nval = 1000;

% Initialize the random number generator



% Load training and validation data
[Pt, Tt] = loadliver;
[Pv, Tv] = loadliverval; 

nodes = 2;

for k=6:6



% Ask for training method
% disp(sprintf('Choose metod:'));
% disp(sprintf('Gradient descent backprop.                  = 1'));
% disp(sprintf('Gradient descent backprop. with momentum    = 2'));
% disp(sprintf('Gradient descent backprop. with adaptive LR = 3'));
% disp(sprintf('Gradient descent backprop. with [2] and [3] = 4'));
% disp(sprintf('Polak-Ribiére conjugate gradient            = 5'));
% disp(sprintf('Scaled conjugate gradient                   = 6'));
% disp(sprintf('BFGS quasi-Newton method                    = 7'));
% disp(sprintf('Levenberg-Marquardt method                  = 8'));
% disp(sprintf('RPROP - Resilient backpropagation           = 9'));
% tmp =  input('                        method? (default 4) = ');
tmp = k;
if isempty(tmp) == 1
  method='traingdx';
elseif tmp == 1
  method='traingd';
elseif tmp == 2
  method='traingdm';
elseif tmp == 3
  method='traingda';
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

% Ask for the error function
%tmp = input('Error function: (MSE=1, CE=2)? [1] ');
%if isempty(tmp) == 1%
%  errFcn='mse';
%elseif tmp == 1
%  errFcn='mse';
%elseif tmp == 2
  errFcn='mse';
%end

% Ask for the number of hidden nodes
%tmp = input('Number of hidden nodes? [2] ');
%if tmp > 0
%  nodes=tmp;
%else
%  nodes=2;
%end

% Ask for the number of epochs
%tmp = input('Number of epochs? [300] ');
%if tmp > 0
%  epoch=tmp;
%else
  epoch=300;
%end

% fprintf('nodes\t totv\t specv\t sensv\t bacv\n');
for i=11:11
nodes = i;
  rand('state',2);
  randn('state',2);
  
% Create a MLP using feedforwardnet
net = feedforwardnet(nodes, method);

% Set the error function
net.performFcn = errFcn;

% Set the correct activation functions
net.layers{1}.transferFcn = 'tansig';
net.layers{2}.transferFcn = 'logsig';

% Other options 
net.outputs{2}.processFcns = {};    % To avoid rescaling of outputs
net.trainParam.showCommandLine = 0; % To show the error on the commandline
net.trainParam.epochs = epoch;      % Number of epochs
net.trainParam.max_fail = epoch+1;  % Avoid early stopping
%net.trainParam.min_grad = 0;        % Avoid stopping because of small gradient

% This is to make train show the validation errors during training
net.divideFcn = 'divideind';                     % Divide according to an index
net.divideParam.trainInd = [1:length(loadliver)];            % The first Ndata will
                                                 % be training
net.divideParam.valInd = [length(loadliver) + 1 : length(loadliver)+ length(loadliverval)];   % The next Nval (1000) will be validation

%Train the network
Pc = [Pt Pv];          % This is both the training and the validation set
Tc = [Tt Tv];          % They are divided during the training according
                       % to the indexes above
net = train(net,Pc,Tc); 

% Plot the results
%plotres(net,Pt,Tt,'Training data');
%plotres(net,Pv,Tv,'Validation data');
%boundary(net,Pt,Tt,500,0.5,0.01);

% Perform some analysis on the result
Y = sim(net,Pt);
[spec, sens, tot, None, Nzero, miss, bac] = stat(Y, Tt);

%disp(sprintf('Results for the training:'));
%disp(sprintf('Total number of data: %d (%d ones and %d zeros)', ...
%	     length(Tt), None, Nzero)); 

%disp(sprintf('Number of misses: %d (%5.2f%% performance)', miss, tot));
%disp(sprintf('Specificity: %5.2f%% (Success for class 0)', spec));
%disp(sprintf('Sensitivilde ty: %5.2f%% (Success for class 1)', sens));
%disp(sprintf('Average Sens and     Spec: %5.2f%%', bac));

% The validation data
Yv = sim(net,Pv);
[specv, sensv, totv, Nonev, Nzerov, missv, bacv] = stat(Yv, Tv);

%disp(sprintf('\nResults for the validation:'));
%disp(sprintf('Total number of data: %d (%d ones and %d zeros)', ...
%	     length(Tv), Nonev, Nzerov)); 

% 
% fprintf('%.2f%',totv);
% fprintf('\t\n')
% 
% fprintf('\t');
fprintf('%.0f%',nodes);
fprintf('\t');
fprintf('%.2f%',specv);
fprintf('\t\n')
fprintf('\t');
fprintf('%.2f%',sensv);
% fprintf('\t');
% fprintf('%.2f%',bacv);

end
fprintf('\t\n')
fprintf('\t\n')
end

