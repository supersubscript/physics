clear all;
close all;

% Default parameters
def_size = [6 6];
def_ndata = 200;
def_epoch = 300;

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

% Give the size of the output grid
tmp = input(sprintf('Give the size of the output grid [%d %d] ', ...
		    def_size(1), def_size(2)));
if tmp > 0
  gsize = tmp;
else
  gsize = def_size;
end

% Hom many epochs
tmp = input(sprintf('Number of epochs? [%d] ', def_epoch));
if tmp > 0
  epoch = tmp;
else
  epoch = def_epoch;
end
noepoch = 0;

% Create the SOFM network
epochs_ord = 300; % ordering phase ecpochs
nhSize_ord = 3;   % Initial neighboghood size diring ordering phase
net = selforgmap(gsize, epochs_ord, nhSize_ord, 'hextop','linkdist');

% Initial training to see the stating point
net.trainParam.epochs = 0;
net = train(net,P);
plotsompos(net,P);

disp('Hit a key to continue');
pause;

% Start to train the network
net.trainParam.epochs = epoch;
net = train(net,P);

% Plot the final result, weight positions and sample hits
plotsompos(net,P);
plotsomhits(net,P);
