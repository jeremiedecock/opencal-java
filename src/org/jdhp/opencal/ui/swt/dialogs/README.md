# Dialogs

## Inheritance

* Dialog
    * AboutDialog
    * PreferencesDialog
    * VirtualKeyboardDialog
    * InsertDialog (abstract)
        * InsertFileDialog (abstract)
            * InsertAudioDialog
            * InsertVideoDialog
            * InsertImageDialog
                * InsertScriptDialog (abstract)
                    * InsertDotDialog (final)
                    * InsertLatexDialog (final)
                    * InsertPlotDialog (final)

## Typical scenario

modifyText [InsertFileDialog]

okButton [InsertFileDialog]
* buildTag()

## TODO

* InsertFileDialog
    * put metadata (src, author, ...) in an XML file (or YAML or JSON or ...) having the same basename than the image (HASHSUM.xml)
* InsertScriptDialog
    * put the source in a flat file (or YAML or JSON or ...) having the same basename than the image and having the extension ".src" (HASHSUM.src)
    * add the script type (latex, dot, gnuplot, python, ...) in the metadata
