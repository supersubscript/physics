function Sat = GBGetSaturation(ndim,v)

% Calculate the saturation
Sat=v'*v;
Sat=Sat / ndim;
