function [P,ims] = loadfacesORL(size);

% function [P,ims] = loadfacesORL;
% function [P,ims] = loadfacesORL(size);
%
% Loads the ORL faces database (80 images). The optional size parameter
% gives you an opertunity to make the images smaller, by rescaling the image
% with "size".  The images are stored columnwise in P and ims is the size
% of the returned images.
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

if nargin == 0
  chsize = 0;
  ims(1,1) = 112;
  ims(1,2) = 92;
else
  if size > 1 
    error('size must be <= 1');
  end
  chsize = 1;
  ims(1,1) = floor(112*size);
  ims(1,2) = floor(92*size);
end

% The filenames
fname = ['X0101'; 'X0601'; 'X1101'; 'X1601'; 'X2101'; 'X2601'; ...
	 'X3101'; 'X3601'; 'X0102'; 'X0602'; 'X1102'; 'X1602'; ... 
	 'X2102'; 'X2602'; 'X3102'; 'X3602'; 'X0201'; 'X0701'; ... 
	 'X1201'; 'X1701'; 'X2201'; 'X2701'; 'X3201'; 'X3701'; ...
	 'X0202'; 'X0702'; 'X1202'; 'X1702'; 'X2202'; 'X2702'; ... 
	 'X3202'; 'X3702'; 'X0301'; 'X0801'; 'X1301'; 'X1801'; ... 
	 'X2301'; 'X2801'; 'X3301'; 'X3801'; 'X0302'; 'X0802'; ... 
	 'X1302'; 'X1802'; 'X2302'; 'X2802'; 'X3302'; 'X3802'; ...
         'X0401'; 'X0901'; 'X1401'; 'X1901'; 'X2401'; 'X2901'; ... 
	 'X3401'; 'X3901'; 'X0402'; 'X0902'; 'X1402'; 'X1902'; ... 
	 'X2402'; 'X2902'; 'X3402'; 'X3902'; 'X0501'; 'X1001'; ... 
	 'X1501'; 'X2001'; 'X2501'; 'X3001'; 'X3501'; 'X4001'; ...
	 'X0502'; 'X1002'; 'X1502'; 'X2002'; 'X2502'; 'X3002'; ...
	 'X3502'; 'X4002';];

text = sprintf('Loading 80 faces of size %d x %d.', ims(1,1),ims(1,2));
disp([text ' This may take some time ...']);

for i=1:80
  filename = ['./FaceORL/' fname(i,:) '.dat'];
  Z = load(filename);
  Z = Z';
  if chsize == 1
    ztmp = imresize(Z,ims,'bilinear');
    P(:,i) = ztmp(:);
  else
    P(:,i) = Z(:);
  end
end
