package com.metrixware.gradle.pandoc.eclipse;

import java.util.List;

import com.google.common.collect.Lists;

public class Section {
	private int level;
	private String name;
	private String label;
	private List<Section> subsections;

	public Section(String name, String label) {
		super();
		this.name = name;
		this.label = label;
		this.subsections = Lists.newArrayListWithExpectedSize(5);
	}

	public List<Section> getSubsections() {
		return subsections;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLabel() {
		return label;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public void addSubsection(Section section) {
		subsections.add(section);
		section.setLevel(level + 1);
	}

	@Override
	public String toString() {
		StringBuilder sb =  new StringBuilder();
		for(int i=0;i<level;++i){
			sb.append(" | ");
		}
		sb.append(name).append("\n");
		for(Section child : subsections){
			sb.append(child.toString());
		}
		return sb.toString();
	}
}
