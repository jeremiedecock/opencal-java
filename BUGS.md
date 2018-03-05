# KNOWN BUGS

- Copy/paste from/to clipboard doesn't work when the program is called from a tmux shell (for both SWT and JavaFX)
- The formatting of XML Transformer has changed in Java9 and it's a mess... See http://java9.wtf/xml-transformer/ for more explanations.
  - In Java8 "transformer.setOutputProperty(OutputKeys.INDENT, "yes");" indents
    the text with 0 spaces by default. In Java9 it indents the text with 4
    spaces by default.
  - The 0 space indent can be retrieved using the following line:
    "transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
    "0");"
  - But the actual issue is that whatever the number of space used for the
    indentation, now (in Java9) the first line of CDATAs is indented too ->
    this add a "\n" at the beginning of each question/answer...
  - Leading '\n' and spaces could be left in the PKB files and simply removed
    in "Card" constructors... But I would like to avoid such a big change in
    the PKB file to avoid possible hidden data loss or hidden data bugs...
  - "transformer.setOutputProperty(OutputKeys.INDENT, "no");" could be used in
    "org.jdhp.opencal.data.pkb.DOMPersonalKnowledgeBase.save()" to completely
    remove the indentation but it's less convenient to version PKB files with
    Git (making bigger diffs) and again it might introduce hidden bugs on the
    data...
  - A better solution would be to write files "manually" in the "save" function
    (i.e. without using the DOM API which is not really useful there)...
  - By the way, it may be the time to consider replacing DOM by SAX for XML
    reading too...
  - In any cases, many tests have to be done with care to avoid data loss or
    any other bug on data!
