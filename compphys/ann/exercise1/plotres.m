function plotres(net,P,T,mytext);

% plotres(net,P,T);
% plotres(net,P,T,mytext);
%
% This function shows the result for a classification task. It works only
% for 2-dimensional problems with 2 classes. The dataset is plotted and
% the different classes are marked by "o" and "+". The correct classified
% datapoints are blue and the misclassified are red.
%
% Inputs:
% net = the network
% P = the inputs
% T = the targets
% mytext = optional string that is prepended on the title of the figure.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Some error checking
if nargin < 3, error('Not enough arguments.'),end
[pr,pc] = size(P);
[tr,tc] = size(T);
if (pr ~= 2), error('P must have 2 rows.'), end
if (tr ~= 1), error('T must have 1 row.'), end

% Supplied mytext or not
if nargin == 3
  mytext = '';
end

% Simulate the network to get the result
Y = sim(net,P);

% Find the two classes
idx1 = find(T>0.5);
idx0 = find(T<0.5);

% Find Mismatches
miss = find(round(Y) ~= T);
miss0 = find(T(miss) < 0.5);
miss1 = find(T(miss) > 0.5);

% Now plot the dataset
figres = figure;
title([mytext ':' 'Red = Misclassified : Blue = Correct classified']);
hold on;
plot(P(1,idx0),P(2,idx0),'bo');
plot(P(1,idx1),P(2,idx1),'b+');

plot(P(1,miss(miss0)),P(2,miss(miss0)),'ro');
plot(P(1,miss(miss1)),P(2,miss(miss1)),'r+');

% Fix the axis
minx = min(P(1,:));
maxx = max(P(1,:));
miny = min(P(2,:));
maxy = max(P(2,:));
edgx = (maxx-minx)*0.4+0.1;
edgy = (maxy-miny)*0.4+0.1;
v = [minx-edgx maxx+edgx miny-edgy maxy+edgy];
axis(v);
grid on;

hold off;

