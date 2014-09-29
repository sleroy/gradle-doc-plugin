
package com.metrixware.gradle.pandoc



class Template {
	String name
	String[] outputs=['html']

	Template(String name){
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
