# KNOWN BUGS

1. Copy/paste from/to clipboard doesn't work when the program is called from a tmux shell (for both SWT and JavaFX)
2. The formatting of XML Transformer has changed in Java9 and it's a mess... See
   http://java9.wtf/xml-transformer/ for more explanations.
    - In Java8 `transformer.setOutputProperty(OutputKeys.INDENT, "yes");` indents
      the text with 0 spaces by default. In Java9 it indents the text with 4
      spaces by default.
    - The 0 space indent can be retrieved using the following line:
      `transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0");`
    - But the actual issue is that whatever the number of space used for the
      indentation, now (in Java9) the first line of CDATAs is indented too ->
      this add a '\n' at the beginning of each question/answer...
    - Leading '\n' and spaces could be left in the PKB files and simply removed
      in "Card" constructors... But I would like to avoid such a big change in
      the PKB file to avoid possible hidden (in git diffs) data loss or hidden
      data bugs...
    - `transformer.setOutputProperty(OutputKeys.INDENT, "no");` could be used in
      `org.jdhp.opencal.data.pkb.DOMPersonalKnowledgeBase.save()` to completely
      remove the indentation but it's less convenient to version PKB files with
      Git (making bigger diffs) and again it might introduce hidden bugs on the
      data...
    - A better solution would be to write files "manually" in the "save" function
      (i.e. without using the DOM API which is not really useful there)...
    - By the way, it may be the time to consider replacing DOM by SAX for XML
      reading too...
    - In any cases, many tests have to be done with care to avoid data loss or
      any other bug on data!
3. Broken links in generated HTML since Java9 (only checked on MacOSX)
    - Cause: the last slash that separates the file name to its directory is
      missing...
    - Example:
      `<img src="/home/joe/pkb/materials74335472e713709aaac2eccdd679cb0b.png" />`
      insead of
      `<img src="/home/joe/pkb/materials/74335472e713709aaac2eccdd679cb0b.png" />`
    - Files where the issue should be fixed: grep -r "img src" src/org/jdhp/opencal/ui/
4. Since Java9, when a card is created with an image, the image is not written
   on the disk (probably the same for audio and video files). Only checked on MacOSX.
    - Cause: the same than for the previous bug... The last slash that separates
      the file name to its directory is missing...
    - Example: "materials1363b17c33740b7241aecd7cec5c381d.png" insead of
      "materials/1363b17c33740b7241aecd7cec5c381d.png"
    - See https://stackoverflow.com/questions/34459486/joining-paths-in-java for
      a Java equivalent to Python "os.path.join(...)"
