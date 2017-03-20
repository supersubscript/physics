function lvqres(net,P,T,mytext,newfig,splot);

% plotres(net,P,T);
% plotres(net,P,T,mytext);
%
% This function shows the result for a classification task. It works only
% for 2-dimensional problems with 2 classes. The dataset is plotted and
% the different classes are marked by "o" and "+". The correct classified
% datapoints are blue and the misclassified are red. The reference
% vectors are also shown.
%
% Inputs:
% net = the network
% P = the inputs
% T = the targets
% mytext = optional string that is prepended on the title of the figure.
%
% Dec 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Some error checking
if nargin < 3, error('Not enough arguments.'),end
[pr,pc] = size(P);
[tr,tc] = size(T);
if (pr ~= 2), error('P must have 2 rows.'), end
if (tr ~= 2), error('T must have 2 rows.'), end

% Supplied mytext, newfig, splot or not
if nargin == 3
  mytext = '';
  newfig = 0;
  splot = 0;
end
if nargin == 4
  newfig = 0;
  splot = 0;
end
if nargin == 5
  splot = 0;
end


% Simulate the network to get the result
Y = sim(net,P);
Y = vec2ind(Y);

% Find the two classes
T = vec2ind(T);
idx1 = find(T==2);
idx0 = find(T==1);

% Find Mismatches
miss = find(round(Y) ~= T);
if isempty(miss) ~= 1
  miss0 = find(T(miss) == 1);
  miss1 = find(T(miss) == 2);
  missplot = 1;
else
  missplot = 0;
end

% Now plot the dataset
if newfig == 0
  if splot > 0 
    subplot(2,1,splot);
    hold on;
  end
else
  figres = figure;
  if splot > 0 
    subplot(2,1,splot);
    hold on;
  end
end
plot(P(1,idx0),P(2,idx0),'bo');
plot(P(1,idx1),P(2,idx1),'b+');
if missplot == 1
  plot(P(1,miss(miss0)),P(2,miss(miss0)),'ro');
  plot(P(1,miss(miss1)),P(2,miss(miss1)),'r+');
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

title([mytext [' : Red=Wrong : Blue=Correct : Green="o" clusters : Yellow='] ...
	       [' "+" clusters']]);
grid on;
