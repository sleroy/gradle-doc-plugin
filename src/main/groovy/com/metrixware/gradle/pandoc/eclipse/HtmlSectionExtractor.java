package com.metrixware.gradle.pandoc.eclipse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlSectionExtractor {

	public static Section extract(String content, int max) {

		Section toc = new Section("TOC", "TOC");
		extract(toc, content, 1, max, 0, content.length() - 1, false);
		return toc;
	}

	private static void extract(Section parent, String content, int level, int max,
			int start, int end, boolean stopAtFirstMatch) {
		if (level <= max) {
			String html = content.substring(start, end);
			Pattern mainPattern = getSectionPattern(level);
			Matcher mainMatcher = mainPattern.matcher(html);

			boolean stop = false;
			while (!stop && mainMatcher.find()) {

				Section section = buildSection(parent, mainMatcher);

				// initialize match zone from the next match occurrence
				int mstart = mainMatcher.start();
				int mend = html.length();
				boolean hasNext = mainMatcher.find();
				if (hasNext) {
					mend = mainMatcher.start();
				}

				String contentSubstring = html.substring(mstart, mend);
				// extract all subsections for the current match zone
				extract(section, contentSubstring, level + 1, max, 0,
						contentSubstring.length(), false);

				if (!stopAtFirstMatch && hasNext) {
					// since we've skipped the next match to detect current
					// match end we need to do a specific extraction only for
					// the next match.
					extract(parent, html, level, max, mainMatcher.start(),
							html.length(), true);
				}
				if (stopAtFirstMatch) {
					stop = true;
				}
			}
		}
	}

	private static Section buildSection(Section parent, Matcher mainMatcher) {
		String name = mainMatcher.group(1);
		String label = mainMatcher.group(2);
		Section section = new Section(name, label);
		parent.addSubsection(section);
		return section;
	}

	private static Pattern getSectionPattern(int level) {
		return Pattern.compile("<section id=\"([^\"]*)\" class=\"level" + level
				+ "[^\"]*\">[^<]*<h" + level + ">([^<]*)</h" + level + ">");
	}
}
