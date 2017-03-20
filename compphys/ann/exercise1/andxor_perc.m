% andxor_perc
%
% Train a Perceptron, using perceptron learning,
% to do the 2-input AND or XOR function.
%
% Nov 2013, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Clear all thing before
clear all;
close all;

% Define the problem
tmp=input('And problem (1) or Xor problem (2)? [1] ');
if tmp > 0
  problem=tmp;
else
  problem=1;
end

if problem == 1
  P = [0 0 1 1;
    0 1 0 1];
  T = [0 0 0 1];
else  P = [0 0 1 1;
    0 1 0 1];
  T = [0 1 1 0];
end  

% Plot the vectors
plotpv(P,T);
drawnow;

% Create a simple percptron with initialization
net = perceptron;

% Train this network with adapt
net.trainParam.epochs = 50;
net = train(net, P, T);

% Get the output values
Y = sim(net,P);
disp(sprintf('The output from the network is:'));
disp(Y);
disp(sprintf('It should be:'));
disp(T);

% Plot the weights as lines
plotpc(net.iw{1,1}, net.b{1});
