% xor_mlp
%
% Train an MLP to do the 2-input XOR function.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Clear all thing before
clear all;
close all;
correct = 0; 

% Define the problem
P = [0 0 1 1;
     0 1 0 1];
T = [0 1 1 0];

% Ask for training method
disp(sprintf('Choose metod:'));
disp(sprintf('Gradient descent backprop.                  = 1'));
disp(sprintf('Gradient descent backprop. with momentum    = 2'));
disp(sprintf('Gradient descent backprop. with adaptive LR = 3'));
disp(sprintf('Gradient descent backprop. with [2] and [3] = 4'));
disp(sprintf('Polak-Ribi�re conjugate gradient            = 5'));
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
tmp = input('Error function: (MSE=1, CE=2)? [1] ');
if isempty(tmp) == 1
  errFcn='mse';
elseif tmp == 1
  errFcn='mse';
elseif tmp == 2
  errFcn='crossentropy';
end

% Ask for the number of hidden nodes
tmp = input('Number of hidden nodes? [2] ');
if tmp > 0
  nodes=tmp;
else
  nodes=2;
end

% Ask for the number of epochs
tmp = input('Number of epochs? [300] ');
if tmp > 0
  epoch=tmp;
else
  epoch=300;
end

% Ask for the number of simulations
tmp = input('Number of simulations? [100] ');
if tmp > 0
  simnr=tmp;
else
  simnr=100;
end


for i = 1:simnr
    
    % Create a MLP with initialization
    net = feedforwardnet(nodes, method);    
    
    % Set the error function
    net.performFcn = errFcn;
    
    % Set the correct activation functions
    net.layers{1}.transferFcn = 'tansig';
    net.layers{2}.transferFcn = 'logsig';
    
    net.trainParam.showWindow=0;
    net.outputs{2}.processFcns = {};    % To avoid rescaling of outputs
    net.trainParam.showCommandLine = 0; % To show the error on the commandline
    net.trainParam.epochs = epoch;      % Number of epochs
    net.divideFcn = '';                 % To avoid division of the data into validation and test  

    % Train this network with train
    net = train(net,P,T);
    
    % Simulate
    Y = sim(net,P);
    % disp(sprintf('The output from the network is:'));
    % disp(Y);
    % disp(sprintf('It should be:'));
    % disp(T);
    if Y(1)>T(1)-0.1 && Y(1)<T(1)+0.1
        if Y(2)>T(2)-0.1 && Y(2)<T(2)+0.1
            if Y(3)>T(3)-0.1 && Y(3)<T(3)+0.1
                if Y(4)>T(4)-0.1 && Y(4)<T(4)+0.1
                    correct = correct + 1;
                end
            end
        end
    % Plot the result
    % plotres(net,P,T);
    end
end
disp(correct);
disp(correct/simnr);
