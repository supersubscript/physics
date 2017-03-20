function showfacecluster(net,type,P,ims,cluster, w, l);

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
if type == 'comp'
  nodes = net.outputs{1,1}.size;
  net.b{1,1} = zeros(nodes,1);
end
%Y = sim(net,P);
Y = net(P);
Yc = vec2ind(Y);

idx = find(Yc == cluster);

if isempty(idx) 
  disp(['Output node ' cluster 'got zero faces !!']);
else
    
  newfig = figure;  
  Nface = length(idx);
  colormap(gray);
    tmp = reshape(w(l,:),ims(1),ims(2));
    imagesc(tmp);
  
end
