clear all;
close all;

disp(sprintf('******** The Travelling Salesman Problem ********\n'));

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% Here we select the size of the TSP problem
tmp=input('How many cities N (default N=25): ');
if tmp > 0
  size=tmp;
else
  size=25;
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
  disp(sprintf(' Elastic Snake Method = 1'));
  disp(sprintf('    Mean Field method = 2'));
  disp(sprintf('         Both methods = 3'));
  tmp=input('             method? (default 3): ');
  if( tmp > 0 )
    method=tmp;
  else
    method=3;
  end

  % Elastic Snake Method
  if( method==3 | method==1 )
    disp(sprintf('\nParameters for the Elastic Snake'));
    
    tmp=input('Number of Hypothetical cities (in units of N)? (default 2.5): ');
    if( tmp > 0 )
      ESHyp=tmp;
    else
      ESHyp=2.5;
    end
    
    tmp=input('Start temperature? (default 0.1): ');
    if( tmp > 0 )
      ESTemp=tmp;
    else
      ESTemp=0.1;
    end
    
    tmp=input('Decrease of temperature? (default 0.95): ');
    if( tmp > 0 )
      ESdTemp=tmp;
    else
      ESdTemp=0.95;
    end
    
    tmp=input('Alpha? (default 0.6): ');
    if( tmp > 0 )
      ESKappa=tmp;
    else
      ESKappa=0.6;
    end

    tmp=input('Decrease of Alpha? (default 0.97): ');
    if( tmp > 0 )
      ESdKappa=tmp;
    else
      ESdKappa=0.97;
    end

    tmp=input('Eta? (default 0.6): ');
    if( tmp > 0 )
      ESEta=tmp;
    else
      ESEta=0.6;
    end

    tmp=input('Decrease of Eta? (default 0.97): ');
    if( tmp > 0 )
      ESdEta=tmp;
    else
      ESdEta=0.97;
    end

  end
  
  % Mean field parameters
  if( method==3 | method==2 )
    disp(sprintf('\nParameters for the Mean Field Method'));
    tmp=input('Alpha parameter (default 1.1): ');
    if( tmp > 0 )
      MFAlpha=tmp;
    else
      MFAlpha=1.1;
    end
    tmp=input('Start temperature (default 0.25): ');
    if( tmp > 0 )
      MFTemp=tmp;
    else
      MFTemp=0.25;
    end
    tmp=input('Decrease of temperature k (default k=0.99): ');
    if( tmp > 0 )
      MFdTemp=tmp;
    else
      MFdTemp=0.99;
    end
  end
    
  
else
  method=3;

  ESTemp=0.1;
  ESdTemp=0.95;
  ESKappa=0.6;
  ESdKappa=0.97;
  ESEta=0.6;
  ESdEta=0.97;
  ESHyp=2.5;
  
  MFAlpha=1.1;
  MFTemp=0.25;
  MFdTemp=0.99;
end

% Get a random TST problem
Loc=rand(size,2);
Distance=dist(Loc,Loc');


% Here comes the Potts model
if method==2 | method==3

  % Start timing
  tic;
  
  % Lets initiate the potts neurons
  v=ones(size);
  v=v/size+(2*rand(size)-1)*0.001;

  % A new figure window
  FigV=figure('resize','on','Position',[201 405 560 420]);
  subplot(2,1,1);
  xlabel('Number of Iterations');
  ylabel('Saturation');
  drawnow;
  hold on;
  tmp=input('Hit return to start!');
  
  % Start the annealing procedure
  nit=1;
  ok=1;
  temp=MFTemp;
  sat=0;
  del_cut=0.01/size;
  while ok==1
    
    del = del_cut+1;
    while del > del_cut 
      
      % Basic for loop over the potts variables
      vold=v;
      for i=1:size
	
	% Construct the local field vector u
	vl=v(:,[size 1:size-1]);
	vr=v(:,[2:size 1]);
	tmp=vr+vl;
	u=Distance(i,:)*tmp;
	tmp=(sum(v)-v(i,:))*MFAlpha+MFAlpha/2;
	u=u+tmp;
	
	u=-u/temp;
	
	% Update Potts neuron i
	pott=exp(u);
	den=sum(pott); 
	v(i,:)=pott/den;
      end
      
      del=sum(sum(abs(v-vold)))/(size*size);
      
      % Compute the saturation
      sat=sum(sum(v.*v))/size;
      
      % Plot the saturation
      subplot(2,1,1);
      plot(nit,sat,'bo');
      hold on;
      
      % Plot the most probable tour 
      subplot(2,1,2);
      [dummy,idx]=max(v);
      plot(Loc(idx,1),Loc(idx,2),'r-');
      hold on;
      plot(Loc([idx(size) idx(1)],1),Loc([idx(size) idx(1)],2),'r-');
      hold on;
      plot(Loc(:,1),Loc(:,2),'b+');
      hold off;
      drawnow;
      
      % Add to counter
      nit=nit+1;
      
    end
    
    if sat>0.99 
      ok=0;
    end
    
    temp=temp*MFdTemp;
    
  end

  vtmp=zeros(size);
  [dummy,idx]=max(v');
  for i=1:size,
    vtmp(i,idx(i))=1;
  end
  v=vtmp;
  vr=v(:,[2:size 1]); % This is the modulo size
  Ep=trace(v'*Distance*vr);
  tmp=sum(v)-1;
  Ec=tmp*tmp';
  if Ec > 0.001
    MFValid=0;
  else
    MFValid=1;
  end
  Etot=Ep+Ec;

  % End timing
  cpu=toc;

  disp(sprintf('\nResults from the Mean Field Method:'));
  disp(sprintf('-----------------------------------'));
  disp(sprintf('The tour lenght: %f', Ep));
  disp(sprintf('Valid Solution: %d (should be 1)', MFValid));
  disp(sprintf('Cpu Time: %f', cpu));

  hold on;
  title('The Mean Field Solution');
  drawnow;

end


% Here comres the elastic snake method
if method==1 | method==3 
  
  % New Figure
  FigTsp=figure('resize','on','Position',[301 405 560 420]);
  
  % Start timing
  tic;
  
  % Init things
  radius=0.1;
  
  % Initiate the ring
  cm=mean(Loc);
  wsize=round(size*ESHyp);
  tmp=0:(wsize-1);
  tmp=tmp*(2*pi)/wsize;
  w(:,1)=cm(1)+radius*cos(tmp');
  w(:,2)=cm(2)+radius*sin(tmp');
  
  % Plot the cities
  plot(Loc(:,1),Loc(:,2),'b*');
  hold on;
  plot(w(:,1),w(:,2),'r+-');
  plot([w(wsize,1);w(1,1)],[w(wsize,2);w(1,2)],'r+-');
  hold off;
  drawnow;

  % Start the process
  ok=1;
  temp=ESTemp;
  nit=0;
  tmp=input('Hit return to start!');
  while ok==1
    
    nit=nit+1;
    % prepare the weights
    tmp=dist(Loc,w');
    sdist=tmp.*tmp;
    sdist=-sdist/(2*temp*temp);
    offset=max(sdist');
    sdist=sdist-(offset')*ones(1,wsize);
    A=exp(sdist);
    den=sum(A');
    den=den';
    den=den*ones(1,wsize);
    if sum(den) == 0
        tmp=input('Hit return to start!');
    end
    A=A./den;
    wold=w;
    for i=1:wsize,
      csumi=sum(A(:,i));
      dw=-w(i,:)*csumi;
      tmp2(:,1)=Loc(:,1).*A(:,i);
      tmp2(:,2)=Loc(:,2).*A(:,i);
      dw=dw+sum(tmp2);
      wrl=w([wsize,1:wsize,1],:);
      tmp=ESKappa*(wrl(i+2,:)-2*wrl(i+1,:)+wrl(i,:));
      dw=dw+tmp;
      dw=ESEta*dw;
      w(i,:)=w(i,:)+dw;
    end
    
    % Calculate diff
    change=sum(sum(abs(w-wold)));

    % Plot the new posistions
    plot(Loc(:,1),Loc(:,2),'b*');
    hold on;
    plot(w(:,1),w(:,2),'r+-');
    plot([w(wsize,1);w(1,1)],[w(wsize,2);w(1,2)],'r+-');
    hold off;
    drawnow;

    % Update the parameters
    temp=temp*ESdTemp;
    ESKappa=ESKappa*ESdKappa;
    ESEta=ESEta*ESdEta;
    
    % Check convergence
    if change<0.001
      ok=0;
    end
  end
  
  % Extract the solution
  [v,status]=TspGetSolution(w,Loc,size);
  vr=v(:,[2:size 1]); % This is the modulo size
  Ep=trace(v'*Distance*vr);
  tmp=sum(v)-1;
  Ec=tmp*tmp';
  if Ec > 0.001
    ESValid=0;
  else
    ESValid=1;
  end
  Etot=Ep+Ec;
  
  % Plot the final snake
  subplot(2,1,1);
  plot(Loc(:,1),Loc(:,2),'b*');
  hold on;
  plot(w(:,1),w(:,2),'r+-');
  plot([w(wsize,1);w(1,1)],[w(wsize,2);w(1,2)],'r+-');
  hold on;
  drawnow;
  
  % Plot the solution found by the snake
  subplot(2,1,2);
  [dummy,idx]=max(v);
  plot(Loc(idx,1),Loc(idx,2),'r-');
  hold on;
  plot(Loc([idx(size) idx(1)],1),Loc([idx(size) idx(1)],2),'r-');
  hold on;
  plot(Loc(:,1),Loc(:,2),'b+');
  hold off;
  drawnow;
  
  % End timing
  cpu=toc;
  
  disp(sprintf('\nResults from the Elastic Snake Method:'));
  disp(sprintf('--------------------------------------'));
  disp(sprintf('The tour lenght: %f', Ep));
  disp(sprintf('Valid Solution: %d (should be 1)', ESValid));
  disp(sprintf('Cpu Time: %f', cpu));

  hold on;
  title('The Elastic Snake Solution');
  drawnow;
  
end




