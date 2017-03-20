% Clear all things
clear all;
close all;

% Load training and validation data
[P,T,Yr] = loadsuns(1);
[Pv,Tv,Yrv] = loadsuns(2);

% Normalize the data to zero mean and unit variance
sigma = std(P);
mu = mean(P);

Pn = (P - mu)/sigma;
Pvn = (Pv - mu)/sigma;
Tn = (T - mu)/sigma;
Tvn = (Tv - mu)/sigma;

% Now matlab wants time sequences in cell arrays
Pc = con2seq(Pn);
Pvc = con2seq(Pvn);
Tc = con2seq(Tn);
Tvc = con2seq(Tvn);
% 
% % Ask for training method
% disp(sprintf('Choose metod:'));
% disp(sprintf('Gradient descent (momentum and adaptiv lr)  = 1'));
% disp(sprintf('Powell-Beale conjugate gradient             = 2'));
% disp(sprintf('Levenberg-Marquardt                         = 3'));
% tmp =  input('                        method? (default 3) = ');
tmp = 3;
if isempty(tmp) == 1
  method='trainlm';
elseif tmp == 1
  method='traingdx';
elseif tmp == 2
  method='traincgb';
elseif tmp == 3
  method='trainlm';
end

% Ask for the number of hidden nodes
% tmp = input('Number of hidden nodes? [2] ');
tmp = 2;
if tmp > 0
  nodes=tmp;
else
  nodes=2;
end

% Use regularization
regul = 0.0;
% tmp = input('Use regularization? (1/0) [0] ');
tmp = 0; 
if tmp > 0
  tmp = input('Perfomance ratio: [0.1] ');
  if tmp > 0
    regul = tmp;
  else
    regul = 0.1;
  end
end

for l = 1:10

% How many time lags
% tmp = input('Number of delays for the feedback? [1] ');
tmp = l;
if tmp > 0
  lags=tmp;
else
  lags=1;
end

% Train this network with train
% tmp = input('Number of epochs? [200] ');
tmp = 200; 
if tmp > 0
  epoch=tmp;
else
  epoch=200;
end

% Create a NAR network with initialization
net = layrecnet(1:lags, nodes, method);

% Set some parameters
net.performParam.regularization  = regul;         % Regularization 
net.outputs{2}.processFcns       = {};            % To avoid rescaling of outputs
net.trainParam.showCommandLine   = 0;             % To show the error on the commandline
net.divideFcn                    = 'dividetrain'; % To avoid division of the data into validation and test
net.trainParam.epochs            = epoch;         % The number of epochs

% Train
[Xs,Xi,Ai,Ts] = preparets(net,Pc,Tc);
net = train(net,Xs,Ts,Xi,Ai);


%%%%%%%%%%%% Evaluate the trained network %%%%%%%%%%%%%%%

%%%%%%%%% Single Step Prediction, training set %%%%%%
pred = net(Xs,Xi,Ai);

% And take it back to non-cell array form
tmp = seq2con(pred); Tpn = tmp{1,1};
tmp = seq2con(Ts); Tn = tmp{1,1};

% Inverse of normalization
Tp = Tpn*sigma + mu;
T = Tn*sigma + mu;

%%%%%%%%% Single Step Prediction, validation set %%%%%%
[Xsv,Xiv,Aiv,Ts] = preparets(net,Pvc,Tvc);
pred = net(Xsv,Xiv,Aiv);

% And take it back to non-cell array form
tmp = seq2con(pred); Tpvn = tmp{1,1};
tmp = seq2con(Ts); Tvn = tmp{1,1};

% Inverse of normalization
Tpv = Tpvn*sigma + mu;
Tv = Tvn*sigma + mu;

%%%%%%%%% Calculate errors %%%%%%
[N,M] = size(Tp);
[Nv,Mv] = size(Tpv);
etrain = Tp - T;
eval = Tpv - Tv;
NMSEtrain = etrain*etrain' / (M*std(T(1,:))^2);
NMSEval = eval*eval' / (Mv*std(Tv(1,:))^2);
sprintf('%7.4f',NMSEval);

%%%%%%%%% Plot the single step prediction for the training set
% Yr = Yr(lags+1:end);
% Yrv = Yrv(lags+1:end);
% figres = figure;
% subplot(2,1,1);
% plot(Yr,T,'b-')
% hold on
% plot(Yr,Tp,'r--')
% hold off
% Err = sprintf('%7.4f',NMSEtrain);
% title(['Training: Single step prediction (red = network) : NMSE = ' Err]);
% 
% 
% subplot(2,1,2);
% plot(Yrv,Tv,'b-')
% hold on
% plot(Yrv,Tpv,'r--')
% hold off
% Err = sprintf('%7.4f',NMSEval);
% title(['Validation: Single step prediction (red = network) : NMSE = ' Err]);
end