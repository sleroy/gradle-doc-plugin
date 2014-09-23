package com.metrixware.gradle.pandoc.postprocess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A post processor that sequentially process a list of {@link RegexPattern} to
 * transform the input string.
 * 
 * @author afloch
 *
 */
public class RegexProstprocessor extends AbstractStringPostprocessor {

	private List<RegexPattern> patterns;

	public RegexProstprocessor(String encoding, RegexPattern... patterns) {
		super(encoding);
		this.patterns = new ArrayList<RegexPattern>(Arrays.asList(patterns));

	}

	public void addPattern(String from, String to) {
		this.patterns.add(new RegexPattern(from, to));
	}

	protected String process(String inString) {
		
		String output = inString;
		for (RegexPattern entry : patterns) {
			output = output
					.replaceAll(entry.getPattern(), entry.getReplace());
		}
		return output;
	}

}
