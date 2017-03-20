
% Clear all thing before
clear all;
close all;

% Ask for training method
disp(sprintf('Gradient descent backprop. with [2] and [3] = 4'));
disp(sprintf('Polak-Ribiére conjugate gradient            = 5'));
disp(sprintf('Scaled conjugate gradient                   = 6'));
disp(sprintf('BFGS quasi-Newton method                    = 7'));
disp(sprintf('Levenberg-Marquardt method                  = 8'));
disp(sprintf('RPROP - Resilient backpropagation           = 9'));
% tmp =  input('                        method? (default 4) = ');
for i=8:8
for l =9:9

    tmp = l;
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
errFcn='mse';
epoch=250;

% fprintf('nodes\t totv\t specv\t sensv\t bacv\n');
[P0 T0] = loadChTrn;
Ptest = loadChTst;
Ytot = zeros(1,384); % Save output in vector Ytot;
folds = 4;


    nodes = i;
    for k=1:folds %folds-fold cross validation (uniform)
        if k==1
            Pt = P0(:,384/folds+1:end);
            Tt = T0(:,384/folds+1:end);
            Pv = P0(:,1:384/folds);
            Tv = T0(:,1:384/folds);
        elseif k == 0
            Pt = P0(:,1:384-384/folds);
            Tt = T0(:,1:384-384/folds);
            Pv = P0(:,384-384/folds+1:end);
            Tv = T0(:,384-384/folds+1:end);
        else
            Pt = [P0(:,1:(k-1)*384/folds) P0(:,k*384/folds+1:end)];
            Tt = [T0(:,1:(k-1)*384/folds) T0(:,k*384/folds+1:end)];
            Pv = P0(:,(k-1)*384/folds+1:k*384/folds);
            Tv = T0(:,(k-1)*384/folds+1:k*384/folds);
        end
        
        rand('state', 2); % Same starting conditions for all nets
        randn('state', 2);
        
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
        net.trainParam.min_grad = 0;        % Avoid stopping because of small gradient
        net.trainParam.showWindow= 0;
        % This is to make train show the validation errors during training
        net.divideFcn =   'divideind';                     % Divide according to an index
        net.divideParam.trainInd = 1:size(Pt,1);            % The first Ndata will                                                 % be training
        net.divideParam.valInd = size(Pt,1)+1:size(Pt,1) + size(Pv,1);   % The next Nval (1000) will be validation
        
        %Train the network
        Pc = [Pt Pv];          % This is both the training and the validation set
        Tc = [Tt Tv];          % They are divided during the training according
        % to the indexes above
        net = train(net,Pc,Tc);
        Tc = [Tt Tv]; % Recombine after splitting
        % Plot the results
        %plotres(net,Pt,Tt,'Training data');
        %plotres(net,Pv,Tv,'Validation data');
        %boundary(net,Pt,Tt,500,0.5,0.01);
        
        % Perform some analysis on the result
        Y = sim(net,Pc);
        Ytot = Ytot + Y;
    end % end of cross-val
    
    % Average performance on training data
    Ytot = Ytot/folds;
    [spec, sens, tot, None, Nzero, miss, bac] = stat(Y, Tc);
    
    % The validation data
    Ytest = sim(net,Ptest);
    
     plotroc(Tc,Y);
     hold on;
%     hold on; 
%     auc = trapz(spec, 1-spec);
%     [A B C, AUC] = perfcurve([0 1], [sens spec], 1);
    
    % How does printing work?
    fprintf('%.0f%',nodes);
    fprintf('\t');
    fprintf('%.2f%',tot);
    fprintf('\t');
    fprintf('%.2f%',spec);
    fprintf('\t');
    fprintf('%.2f%',sens);
    fprintf('\t');
    fprintf('%.2f%',bac);
    fprintf('\t');
%     fprintf('%.2f%',AUC);
    fprintf('\t\n');
%     disp(Ytot);
end
end

