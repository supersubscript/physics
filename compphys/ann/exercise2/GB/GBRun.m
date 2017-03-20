clear all;
close all;


disp(sprintf('******** The Graphbisection Problem ********\n'));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Here we specify the size of the problem to run
tmp=input('How many nodes N (default N=20): ');
if tmp > 0
  ndim=tmp;
else
  ndim=20;
end

% How many edges ?
tmp=input('The edge-density p*N (default p*N=2.5): ');
if tmp > 0
  pedge=tmp;
else
  pedge=2.5;
end

% The alpha parameter
tmp=input('The alpha parameter (default alpha=1.1): ');
if tmp > 0
  alpha=tmp;
elseif tmp < 0
  alpha=0;
else
  alpha=1.1;
end

% The random generator init
tmp=input('Seed to the random generator (default sum(100*clock)): ');
if tmp > 0
  rand('state',tmp);
else
  rand('state',sum(100*clock));
end


% More options
tmp=input('Do you want to look at more parameters? (y/n) (default n): ','s');
if strcmp(tmp,'y')
  
  % Metod
  disp(sprintf('Choose metod:'));
  disp(sprintf('            Hopfield net = 1'));
  disp(sprintf(' Stochastic Hopfield net = 2'));
  disp(sprintf('       Mean Field method = 3'));
  disp(sprintf('             All methods = 4'));
  tmp=input('                 method? (default 4): ');
  if( tmp > 0 )
    method=tmp;
  else
    method=4;
  end
  
  % Stokastisk Hopfield metod
  if( method==4 | method==2 )
    disp(sprintf('\nParameters for the Stochastic Hopfield net'));
    tmp=input('How many temperatures? (default 5): ');
    if( tmp > 0 )
      NoTemp=tmp;
    else
      NoTemp=5;
    end
    tmp=input('Start temperature? (default 10.0): ');
    if( tmp > 0 )
      Temp(1)=tmp;
    else
      Temp(1)=10.0;
    end
    tmp=input('Final temperature? (default 0.1): ');
    if( tmp > 0 )
      Temp(NoTemp)=tmp;
    else
      Temp(NoTemp)=0.1;
    end
    tmp=input('How many iterations at each temperature? (default 10): ');
    if( tmp > 0 )
      NoThermalize=tmp;
    else
      NoThermalize=10;
    end
    tmp=input('Make T=0 updates (y/n)? (default y): ');
    if strcmp(tmp,'n')
      ZeroTemp=0;
    else
      ZeroTemp=1;
    end
  end
  
  % Mean field parameters
  if( method==4 | method==3 )
    disp(sprintf('\nParameters for the Mean Field Method'));
    tmp=input('Start temperature (default 0.25): ');
    if( tmp > 0 )
      MFTemp=tmp;
    else
      MFTemp=0.25;
    end
    tmp=input('Decrease of temperature k (default k=0.97): ');
    if( tmp > 0 )
      MFdTemp=tmp;
    else
      MFdTemp=0.97;
    end
  end
    
  
else
  method=4;
  NoTemp=5;
  NoThermalize=10;
  Temp(1)=10.0;
  Temp(NoTemp)=0.1;
  ZeroTemp=1;
  
  MFTemp=0.25;
  MFdTemp=0.97;
end


% Get a random problem
T=GBGetRndProblem(ndim,pedge);

% Set the weights
w=T-alpha;

% The methods to use
if method==1 | method==4
  
  % Here is the pure Hopfield net 
  tic;
  
  % Initial node values
  s=ones(ndim,1);
  tmp=rand(ndim,1);
  s([find(tmp<0.5)])=-1;
  
  ok=1;
  nup=0;
  okn=0;
  while ok==1
    sold=s;
    for i=1:ndim,
      idx=floor(ndim*rand)+1;
      v=dot(w(idx,:),s);
      if v==0.0
	v=-1.0;
      end
      s(idx)=sign(v);
      nup=nup+1;
    end
    change=sum(abs(s-sold));
    if change==0 
      okn=okn+1;
    else
      okn=0;
    end
    if okn==5 | nup>(ndim*500);
      ok=0;
    end
  end

  cpu=toc;
  s1=s;
  % Plot the result
  cutH = GBRes(T,s,alpha,ndim,nup,1,cpu,0,0,0,0);
  
end

if method==2 | method==4
  
  % Here is the stochastic Hopfield model
  tic;
  
  % Lets set up the temperatures
  dTemp=exp( log(Temp(NoTemp)/Temp(1))/(NoTemp-1) );
  for i=2:(NoTemp-1),
    Temp(i)=(dTemp^(i-1))*Temp(1);
  end
  
  % Initial node values
  s=ones(ndim,1);
  tmp=rand(ndim,1);
  s([find(tmp<0.5)])=-1;
  TotUp=0;
  
  % Here is the stochastic updating for some T
  NegVPosS=0;
  NegVNegS=0;
  PosVNegS=0;
  PosVPosS=0;
  for TempIt=1:NoTemp,
    
    for NoTherm=1:NoThermalize,
      
      for i=1:ndim,
	idx=floor(ndim*rand)+1;
	v=dot(w(idx,:),s);
	vsign=sign(v);
	if rand < 1/(1+exp(-v/Temp(TempIt)))
	  s(idx)=1;
	  if vsign < 0 NegVPosS=NegVPosS+1; else PosVPosS=PosVPosS+1; end
	else
	  s(idx)=-1;
	  if vsign < 0 NegVNegS=NegVNegS+1; else PosVNegS=PosVNegS+1; end
	end
	TotUp=TotUp+1;
      end
      
    end
    
  end
    
  % Here is the T=0 updating
  if( ZeroTemp==1 )
    ok=1;
    nup=0;
    okn=0;
    while ok==1
      sold=s;
      for i=1:ndim,
	idx=floor(ndim*rand)+1;
	v=dot(w(idx,:),s);
	if v==0.0
	  v=-1.0;
	end
	s(idx)=sign(v);
	nup=nup+1;
      end
      change=sum(abs(s-sold));
      if change==0 
	okn=okn+1;
      else
	okn=0;
      end
      if okn==5 | nup>10000;
	ok=0;
      end
    end
    TotUp=TotUp+nup;
  end
    
  cpu=toc;
  s2=s;
  % Plot the result
  cutSH = GBRes(T,s,alpha,ndim,TotUp,2,cpu,...
      NegVPosS,NegVNegS,PosVNegS,PosVPosS);
    
end
  
% The Mean Field Method
if method==3 | method==4
  tic;

  % Some handy thins
  ettor=ones(ndim,1);
  SatLimit=(ndim-1)/ndim;
  DelLimit=0.01;
  
  % Initite the mean field variables
  v=(2.0*rand(ndim,1)-1.0)*0.001;
  
  [Ep,Ec]=GBGetEnergy(w,v,alpha);
  Sat=GBGetSaturation(ndim,v);
  
  FigMF=figure('resize','on','Position',[61 505 560 420]);
  subplot(2,1,1);
  xlabel('Number of Iterations');
  ylabel('Red = v:s: Blue = Saturation');
  hold on;
  subplot(2,1,2);
  xlabel('Number of Iterations');
  ylabel('Red=Const. En. : Blue = Cost En.');
  hold on;
  
  % Start the annealing procedure
  nit=1;
  ok=1;
  while ok==1
    
    ok2=1;
    while ok2==1
      del=0.0;
      for i=1:ndim,
	v_save=v(i);
	v(i)=0.0;
	dE=-2*w(i,:)*v/ndim;
	v(i)=tanh(-dE/MFTemp);
	del=del+abs(v_save-v(i));
      end
      del=del/ndim;
      
      Sat=GBGetSaturation(ndim,v);
      
      % Plot the development of the neurons
      subplot(2,1,1);
      plot(nit,Sat,'bo');
      plot(nit*ettor,v,'r.');
      hold on;
      
      % Plot the two energy terms in the next window
      [Ep,Ec]=GBGetEnergy(w,v,alpha);
      subplot(2,1,2);
      plot(nit,Ep,'bo');
      plot(nit,Ec,'ro');
      drawnow;
      hold on;
      
      % Add to counter
      nit=nit+1;
      
      % Check del limit
      if del < DelLimit 
	ok2=0;
      end
      
    end
      
    if Sat>SatLimit | nit>500
      ok=0;
    end
    
    MFTemp=MFTemp*MFdTemp;
    
  end
  
  % Get the final solution
  idx1=find(v>0.0);
  idx0=find(v<=0.0);
  v(idx1)=1.0;
  v(idx0)=-1.0;
  s3=v;
  
  cpu=toc;
  % Plot the result
  cutMF = GBRes(T,v,alpha,ndim,nit*ndim,3,cpu,0,0,0,0);
    
end

% Plot the final solution
FigPlot=figure('resize','on','Position',[632 199 560 724]);
hold on;
if method==1 | method==4 
  if( method==4 )
    subplot(3,1,1);
  end
  text = sprintf('Pure Hopfield Network: cut = %d', cutH);
  title(text);
  drawnow;
  hold on;
  GBPlot(s1,T,ndim,1);
end
if method==2 | method==4 
  if( method==4 )
    subplot(3,1,2);
  end
  text = sprintf('Stochastic Hopfield Network: cut = %d', cutSH);
  title(text);
  drawnow;
  hold on;
  GBPlot(s2,T,ndim,2);
end
if method==3 | method==4 
  if( method==4 )
    subplot(3,1,3);
  end
  text = sprintf('Mean Field Method: cut = %d', cutMF);
  title(text);
  drawnow;
  hold on;
  GBPlot(s3,T,ndim,3);
end
