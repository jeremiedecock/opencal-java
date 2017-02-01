/*
 * OpenCAL
 * Copyright (c) 2007,2008,2009,2010,2011,2012,2016,2017 Jérémie DECOCK <jd.jdhp@gmail.com> (www.jdhp.org)
 */

package org.jdhp.opencal.ui.html;

import org.jdhp.opencal.ui.css.CSS;

public class HtmlWrapper {

    public static final String wrapHtmlBody(String html_body, String css) {
        StringBuffer html = new StringBuffer();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");

        // HTML head
        html.append("<head>\n");
        html.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />\n");

        // CSS
        html.append("<style type=\"text/css\" media=\"all\">\n");
        html.append(css);
        html.append("</style>\n");

        // Mathjax
        // Install MathJax on Debian: aptitude install libjs-mathjax
        html.append("<script type=\"text/x-mathjax-config\">\n");
        html.append("MathJax.Hub.Config({\n");
        html.append("    tex2jax: {inlineMath: [[\"$\",\"$\"],[\"\\\\(\",\"\\\\)\"]]}\n");
        html.append("});\n");
        html.append("</script>\n");
        html.append("<script type=\"text/javascript\" src='/usr/share/javascript/mathjax/MathJax.js?config=TeX-AMS_HTML-full'></script>\n");

        html.append("</head>\n");
        
        // HTML body
        html.append("<body>");

        html.append(html_body);
        
        html.append("</body>\n");
        html.append("</html>");
 
        return html.toString();
    }
  
}
