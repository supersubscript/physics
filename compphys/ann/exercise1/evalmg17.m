function NMSE = evalmg17(net,scale,sel);

% NMSE = evalmg17(net,scale);
% NMSE = evalmg17(net,scale,sel);
%
% This function computes Normalized Mean Summed Errors for both single step
% predictions and iterated predictions for the Mackey-Glass data, using the
% supplied network "net" and a dummy model. The dummy model just says that
% the next value is the same as the last value, i.e. S(t+1)=S(t). The
% results is plotted in a figure and the errors are returned in NMSE as
% follows:
%
% NMSE(1) = NMSE for single step pred. on the training set
% NMSE(2) = NMSE for single step pred. on the test set
% NMSE(3) = NMSE for iterated pred. on the test set
% NMSE(4) = NMSE for the dummy single step pred. (x(t+1)=x(t)) on the
%           test set. 
%
% The scale parameter is used to scale the sunspot numbers, i.e. sn(t) ->
% sn(t)/scale. Use this if you did some rescaling before you trained your
% network. If no rescaling was used then use scale = 1.
%
% The optional sel variable are the selections of time lags you used during
% the training. E.g. if you trained you net using t-1, t-6, t-10 then sel =
% [1 6 10].
%
% Nov 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Some checking
if nargin < 1 
  error('You must provide a network');
end
if nargin == 3
  [nr,nc] = size(sel);
  if (nr ~= 1) & (nc > 10)
    error('The second argument must be a row vector max length 10');
  end
end

NMSE = zeros(1,3);

% Load mg17 data
[Po,T] = loadmg17(1);
[Pto,Tt] = loadmg17(2);
Po = Po / scale;
T(1,:) = T(1,:) / scale;
Pto = Pto / scale;
Tt(1,:) = Tt(1,:) / scale;

% Use perhaps only a subset of the time lags
if nargin ~= 3
  sel = [1 2 3 4 5 6 7 8 9 10]';
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
figure = subplot(3,1,1);
plot(T(2,:),T(1,:),'b-')
hold on
plot(T(2,:),Y(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(1));
title(['Training: Single step prediction (red = network) : NMSE = ' Err]);

subplot(3,1,2)
plot(Tt(2,:),Tt(1,:),'b-')
hold on
plot(Tt(2,:),Yt(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(2));
Errd = sprintf('%7.4f',NMSE(4));
title(['Test: Single step prediction (red = network) : NMSE = ' Err  ...
		    ' (dummy ' Errd ' )']);

% Test iterated prediction
PtTmp = Pto(:,1);
Pin = PtTmp(sel,1);
Yti(1,1) = sim(net,Pin);
for i=2:Mt,
  
  % Move all one step forward
  for j=10:-1:2,
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
plot(Tt(2,:),Tt(1,:),'b-')
hold on
plot(Tt(2,:),Yti(1,:),'r--')
hold off
Err = sprintf('%7.4f',NMSE(3));
title(['Test: Iterated prediction (red = network) : NMSE = ' Err ...
       ' (dummy ' Errd ' )']);
