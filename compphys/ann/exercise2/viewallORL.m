function viewallORL(P,ims);

% function viewfaceORL(P,ims);
%
% View the ORL image stored in P with the size ims(1) x ims(2).
%
% Dec 2015, Mattias Ohlsson 
% Email: mattias@thep.lu.se

% Get the correct colormap
colormap(gray);

n1 = 1;
n2 = 80;
nn = 1;
for n=n1:n2
    colormap(gray);
    subplot(3,4,nn);
  
  % Create an image from the vector. 
  Pt = P(:,n);
  tmp = reshape(Pt,ims(1),ims(2));
  
  % show the image with the correct aspect ratio
  imagesc(tmp);
  daspect([ims(2) ims(1) 1]);
  axis off;
  
  if nn == 12
      pause;
      close;
      figure;
      nn = 1;
  else
      nn = nn + 1;
  end
  
end
