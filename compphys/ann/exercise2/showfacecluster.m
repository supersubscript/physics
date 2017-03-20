function showfacecluster(net,type,P,ims,cluster);

% function showfacecluster(net,type,P,ims,cluster);
%
% This function shows all ORL faces that had output node "cluster" as a
% winner. Inputs:
%
% net: The competitive network
% type: should be either 'comp' for a competitive network or
%       'sofm' for a self-organizing feature map.
% P: Training images
% ims: size of the images (coming form the loadfacesORL command)
% cluster: Output node to show faces for
%
% Dec 2015, Mattias Ohlsson
% Email: mattias@thep.lu.se

% Find the winners
% Don't forget to put the biases to zero if a 'comp' network

%Y = sim(net,P);
Y = net(P);
Yc = vec2ind(Y);

idx = find(Yc == cluster);

if isempty(idx) 
  disp(['Output node ' cluster 'got zero faces !!']);
else
  Nface = length(idx);
  sx = 2; sy = 1;
  if Nface > 2 sx = 2; sy = 2; end
  if Nface > 3 sx = 4; sy = 2; end
  if Nface > 8 sx = 5; sy = 3; end
  if Nface > 15 sx = 6; sy = 4; end
  if Nface > 24 sx = 7; sy = 5; end
  if Nface > 35 sx = 8; sy = 6; end
  if Nface > 48 sx = 9; sy = 7; end
  if Nface > 63 sx = 10; sy = 8; end
  newfig = figure;
  colormap(gray);
  for i=1:Nface
    subplot(sy,sx,i);
    tmp = reshape(P(:,idx(i)),ims(1),ims(2));
    imagesc(tmp);
  end
end
