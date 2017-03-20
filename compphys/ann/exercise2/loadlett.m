function P = loadlett(sel,fp);

% function P = loadlett(sel, fp);
%
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

% Load the letters
[Pt,T] = prprob;

% Find the selected
P = Pt(:,sel);

% Flip some bits according to fp
ta = rand(35,1);
tb = zeros(35,1);
tb(find(ta<fp),1) = 1;
P = abs(P(:,1) - tb(:,1));

% Make a (-1,1) representation suitable for
% the Hopfiled network
P = 2*P - ones(35,1);
