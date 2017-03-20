function P = loadletters(no);

% function P = loadletters(no);
%
% This function loads the first no letter of the english (uppercase)
% alphabet. The letters are stored columnwise in P.
%

% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

% Some error checking
if no > 26
  error('Only 26 letters available!');
end

% Load the letters
[Pt,T] = prprob;

% Make a (-1,1) representation suitable for
% the Hopfield network
Pt = 2*Pt - ones(35,26);

% Take the no first
P = Pt(:,[1:no]);

% Just for showing the letters
if 0
  for n=1:26
    subplot(5,6,n);
    x = P(:,n);
    ztmp = reshape(x,5,7)';
    imshow(ztmp,'InitialMagnification','fit');
    title(sprintf('Letter no %d', n)); 
  end
end
