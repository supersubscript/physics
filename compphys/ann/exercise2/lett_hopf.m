clear all;
close all;

% What letter
lettidx = ['A' 'B' 'C' 'D' 'E' 'F' 'G' 'H' 'I' 'J' 'K' 'L' 'M' 'N' 'O' 'P' ...
	  'Q' 'R' 'S' 'T' 'U' 'V' 'W' 'X' 'Y' 'Z'];

% Load the letters
tmp = input('How many letters would you like to put in the memory? [5] ');
if tmp > 0
  NoLetter = tmp;
else
  NoLetter = 10;
end
P = loadletters(NoLetter);

% What Hopfield model
disp(sprintf('What model to use?'));
disp(sprintf('The original Hopfield model   = 1'));
disp(sprintf('Matlabs Hopfield model        = 2'));
tmp =  input('           model? (default 1) = ');
if tmp > 0
  model = tmp;
else
  model = 1;
end

% "Train" a hopfield network
if model == 1
  w = newhop2(P);
else
  net = newhop(P);
end

go = 'y';
while go == 'y'

  % Test the hopfield networks on a distorted letter
  disp('Test the Hopfield network on a distorted letter');
  tmp = input('What letter? (1-26) [1] ');
  if tmp > 0
    ilett = tmp;
  else
    ilett = 1;
  end

  % Give the distorsion factor
  disp('The distorsion factor');
  tmp = input('Probability to flip a bit (0-1) [0.1] ');
  if isempty(tmp) == 1
    dist = 0.1;
  else
    dist = tmp;
  end

  Pd = loadlett(ilett,dist);
  PdTrue = loadlett(ilett,0.0);
  viewlett(Pd,['Initial image (letter ' lettidx(ilett) ' )'],1,1);

  disp('Hit a key to iterate the Hopfield network on this (initial) letter');
  pause;

  Y = Pd;
  dY = 1;
  no = 0;
  while dY > 0,
    if model == 1
      Yt = simhop(w,1,Y);
    else
      Yt = hardlims(sim(net,1,[],Y));
    end
    dY = sum(length(find(Yt ~= Y)));
    Y = Yt;
    no = no + 1;
    score = length(find(Y ~= PdTrue));
    if isempty(score) == 1
      score = 0;
    end
    text = sprintf('Result for iteration %d (%d wrong bits)', no, score);
    viewlett(Y,text,0,2);
    pause(2);
  end
  disp(sprintf('Converged after %d iterations', no));

  tmp = input('Test another letter? [y] ','s');
  if isempty(tmp) == 1
    go = 'y';
  else
    go = tmp;
  end
  
  close;
end
