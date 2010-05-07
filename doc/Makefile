all: cahier_des_charges.pdf maquettes.pdf scénarios.pdf spécifications_générales.pdf

cahier_des_charges.pdf: cahier_des_charges.tex
	pdflatex cahier_des_charges.tex
	pdflatex cahier_des_charges.tex

maquettes.pdf: maquettes.tex
	pdflatex maquettes.tex
	pdflatex maquettes.tex

scénarios.pdf: scénarios.tex
	pdflatex scénarios.tex
	pdflatex scénarios.tex

spécifications_générales.pdf: spécifications_générales.tex
	pdflatex spécifications_générales.tex
	pdflatex spécifications_générales.tex

clean:
	@echo "suppression des fichiers de compilation"
	@rm -f *.log *.aux *.dvi *.toc *.lot *.lof *.out

init: clean
	@echo "suppression des fichiers cibles"
	@rm -f *.pdf
