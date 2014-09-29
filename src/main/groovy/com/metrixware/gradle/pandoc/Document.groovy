
package com.metrixware.gradle.pandoc

import groovy.lang.Closure

import java.util.Map


class Document {
	String name
	String type='md'
	String[] languages=['en']
	String[] templates=['all']

	Document(String name){
		this.name = name
	}

	String getName() {
		return name
	}

	String[] getLanguages() {
		return languages
	}

	boolean support(Template template ){
		if(templates.find {String t -> t.equals('all')}!=null){
			return true
		}
		def collected =  templates.findAll {String t -> template.name.equals(t)}
		def hasTemplate = !collected.isEmpty()
		return hasTemplate
	}

	String getType() {
		return type
	}

	String[] getSupportedTemplates() {
		return templates
	}


	String getDocsDirectory() {
		return docsDirectory
	}

	String getOutputDirectory() {
		return outputDirectory
	}

	String getTmpDirectory() {
		return tmpDirectory
	}

	@Override
	String toString() {
		return name
	}
}
