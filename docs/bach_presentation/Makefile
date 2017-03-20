FIGURES=
DATAFILES=

all: presentation.pdf clean

clean:
	rm -f presentation.{aux,log,nav,out,snm,toc}
	rm *.sty

presentation.pdf: theme presentation.tex figures/wings.png figures/landscape.png figures/complexities-eps-converted-to.pdf $(FIGURES)
	xelatex --toc presentation.tex
	xelatex --toc presentation.tex

#$(FIGURES): figures/plot.m $(DATAFILES) 
#	cd figures &&\
#	wolfram -script plot.m

theme:
	cd mtheme &&\
	make sty &&\
	cp *.sty ..

.PHONY: all clean theme
