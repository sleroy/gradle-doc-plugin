package com.metrixware.gradle.pandoc.postprocess;

public class RegexPattern {
	private String pattern;
	private String replace;
	
	public RegexPattern(String pattern, String replace) {
		super();
		this.pattern = pattern;
		this.replace = replace;
	}

	
	public String getPattern() {
		return pattern;
	}
	
	public String getReplace() {
		return replace;
	}
	@Override
	public String toString() {
		return pattern+"->"+replace;
	}
}
