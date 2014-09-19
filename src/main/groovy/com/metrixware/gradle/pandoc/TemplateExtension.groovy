
package com.metrixware.gradle.pandoc



class TemplateExtension {
	String name
	String[] outputs=['html']

	TemplateExtension(String name){
		this.name = name
	}

	String getName() {
		return name
	}

	String[] getOutputs() {
		return outputs
	}

	@Override
	String toString() {
		return name
	}
}
