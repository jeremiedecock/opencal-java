package org.jdhp.opencal.ui.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jdhp.opencal.data.properties.ApplicationProperties;

public class QuestionAnswerToHtmlImpl implements QuestionAnswerToHtml {
	
	public String questionAnswerToHtml(String question_or_answer) {
		// Empèche l'interprétation d'eventuelles fausses balises comprises dans les cartes
		String html = replaceSpecialChars(question_or_answer);
		
		// Make html image tags
		String pattern = "&lt;img file=&quot;([0-9abcdef]{32}.(png|jpg|jpeg|gif))&quot; /&gt;";
		Pattern regPat = Pattern.compile(pattern);
		Matcher matcher = regPat.matcher(html);
		html = matcher.replaceAll("<img src=\"" + ApplicationProperties.getImgPath() + "$1\" />");
		
		return html;
	}
	
	/**
	 * Replace the five reserved characters with entities
	 * to avoid HTML interpretation of src.
	 * 
	 * @param src the string to process
	 * @return a string without HTML tags
	 */
	public static String replaceSpecialChars(String src) {
		src = src.replaceAll("&", "&amp;");
		src = src.replaceAll("<", "&lt;");
		src = src.replaceAll(">", "&gt;");
		src = src.replaceAll("\"", "&quot;");
		src = src.replaceAll("\'", "&apos;");
		
		return src;
	}
	
}
