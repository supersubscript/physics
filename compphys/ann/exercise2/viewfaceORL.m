function viewfaceORL(P,ims);

% function viewfaceORL(P,ims);
%
% View the ORL image stored in P with the size ims(1) x ims(2).
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

% Get the correct colormap
colormap(gray);

% Create an image from the vector. 
tmp = 0.33 * reshape(P,ims(1),ims(2));

% show the image with the correct aspect ratio
image(tmp);
daspect([ims(2) ims(1) 1]);
