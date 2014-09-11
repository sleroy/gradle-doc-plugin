package com.metrixware.gradle.markdown.postprocess;

public class Latex2HtmlReferencesPostprocessor extends RegexProstprocessor {

	private final static String ANCHOR = "\\<p\\>\\[(.*)\\]\\</p\\>";
	private final static String ANCHOR_REPLACE = " <a name=\"$1\">$1</a>";
	private final static String REFERENCE = "\\[(.*)\\]";
	private final static String REFERENCE_REPLACE = "<a href=\"#$1\">$1</a>";

	public Latex2HtmlReferencesPostprocessor(String encoding) {
		super(encoding);
		addPattern(ANCHOR, ANCHOR_REPLACE);
		addPattern(REFERENCE, REFERENCE_REPLACE);
		addPattern("<a href=(.*)>(\\w*):(.*)</a>", "<a href=$1>$3</a>");
	}

}
