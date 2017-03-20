function viewlett(X,text,newplot,splot)

% function viewlett(X,text)
% function viewlett(X,text,newplot)
% function viewlett(X,text,newplot,splot)
%
% Takes a column vector, which is actually a 7x5 matrix representing a
% letter, and displays it as a black and white image. The optional text
% parameter is displayed as the title of the image.
%
% newplot and splot can be used to view two letters besides each other.
%
% newplot splot  Action
% ------  -----  ------
%   0       0     Shows the letter in an excisting figure
%   1       0     Creates a new figure and shows the letter there.
%   0       1     Shows the image on the left side of an excisting figure.
%   0       2     Shows the image on the right side of an excisting figure.
%   1       1     Creates a new figure and shows the image on the left side.
%   1       2     Creates a new figure and shows the image on the right side.
%
% Default is newplot = 0 and splot = 0 
%
% Dec 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Check the length
[Xm,Xn] = size(X);
if Xm ~= 35
  error('Sorry, only 35 length column vectors into viewlett, please')
end

% Process the arguments
if nargin == 1
  text = '';
  newplot = 0;
  splot = 0;
end
if nargin == 2
  newplot = 0;
  splot = 0;
end
if nargin == 3
  splot = 0;
end

% Unfold the vector
ztmp = reshape(X,5,7)';

% Display it
if newplot == 0
  if splot > 0 
    subplot(1,2,splot);
  end
else
  newfig=figure;
  if( splot > 0 )
    subplot(1,2,splot);
  end
end
imshow(ztmp,'InitialMagnification','fit');
title(text);
