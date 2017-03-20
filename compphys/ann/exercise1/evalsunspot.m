function NMSE = evalsunspot(net,scale,sel);

% NMSE = evalsunspot(net,scale,sel);
% NMSE = evalsunspot(net,scale);
%
% This function computes Normalized Mean Squared Errors for both single step
% predictions and iterated predictions for the Sunspot data, using the
% supplied network "net" and a dummy model. The dummy model just says
% that the next value is the same as the last value, i.e. S(t+1)=S(t). 
% The results are plotted in a figure and the errors are returned
% in NMSE as follows:
%
% NMSE(1) = NMSE for single step pred. on the training set
% NMSE(2) = NMSE for single step pred. on the test set
% NMSE(3) = NMSE for iterated pred. on the test set
% NMSE(4) = NMSE for the dummy single step pred. on the test set
%
% The scale parameter is used to scale the sunspot numbers, i.e. sn(t) ->
% sn(t)/scale. Use this if you did some rescaling before you trained your
% network. If no rescaling was used then use scale = 1.
%
% The optional sel variable are the selections of time lags you used during
% the training. E.g. if you trained you net using t-1, t-6, t-12 then sel =
% [1 6 12]. If not supplied, 12 time lags are used.
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Some checking
if nargin < 2 
  error('You must provide a network and a scale parameter');
end
if nargin == 3
  [nr,nc] = size(sel);
  if (nr ~= 1) & (nc > 12)
    error('The second argument must be a row vector max length 12');
  end
end

NMSE = zeros(1,3);

% Load sunspot data
[Po,T] = loadsunspot(1);
[Pto,Tt] = loadsunspot(2);
Po = Po / scale;
T(1,:) = T(1,:) / scale;
Pto = Pto / scale;
Tt(1,:) = Tt(1,:) / scale;

% Use perhaps only a subset of the time lags
if nargin ~= 3
  sel = [1 2 3 4 5 6 7 8 9 10 11 12]';
end
P = Po(sel,:);
Pt = Pto(sel,:);

% Test single step prediction
Y = sim(net,P);
Yt = sim(net,Pt);
[N,M] = size(Y);
[Nt,Mt] = size(Yt);

% The dummy prediction
Yd(1) = Po(1,1);
Yd(2:Mt) = Tt(1,1:Mt-1);

% Calculate errors
etrain = Y(1,:)-T(1,:);
etest = Yt(1,:)-Tt(1,:);
etestd = Yd(1,:)-Tt(1,:);
NMSE(1) = etrain*etrain'/(M*std(T(1,:))^2);
NMSE(2) = etest*etest'/(Mt*std(Tt(1,:))^2);
NMSE(4) = etestd*etestd'/(Mt*std(Tt(1,:))^2);

% Plot the single step prediction both training and test
figres = figure;
subplot(3,1,1);
plot(T(2,:),scale*T(1,:),'b-')
hold on
plot(T(2,:),scale*Y(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(1));
title(['Training: Single step prediction (red = network) : NMSE = ' Err]);

subplot(3,1,2)
plot(Tt(2,:),scale*Tt(1,:),'b-')
hold on
plot(Tt(2,:),scale*Yt(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(2));
Errd = sprintf('%7.4f',NMSE(4));
title(['Test: Single step prediction (red = network) : NMSE = ' Err ...
		    ' (dummy ' Errd ' )']);

% Test iterated prediction
PtTmp = Pto(:,1);
Pin = PtTmp(sel,1);
Yti(1,1) = sim(net,Pin);
for i=2:Mt,
  
  % Move all one step forward
  for j=12:-1:2,
    PtTmp(j,1)=PtTmp(j-1,1);
  end
  PtTmp(1,1)=Yti(1,i-1);
  
  % Make a new prediction 
  Pin = PtTmp(sel,1);
  Yti(1,i) = sim(net,Pin);
end
  
% Calculate errors
etesti = Yti(1,:)-Tt(1,:);
NMSE(3) = etesti*etesti'/(Mt*std(Tt(1,:))^2);

% Make a plot
subplot(3,1,3)
plot(Tt(2,:),scale*Tt(1,:),'b-')
hold on
plot(Tt(2,:),scale*Yti(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(3));
title(['Test: Iterated prediction (red = network) : NMSE = ' Err ...
       ' (dummy ' Errd ' )']);
