package com.metrixware.gradle.markdown

import java.text.SimpleDateFormat

import org.gradle.api.DefaultTask

class DocumentationTask extends DefaultTask {

	static final String DOCS = 'docs'


	def FOLDER_SCRIPTS = 'scripts'
	def FOLDER_IMAGES= 'images'
	def FOLDER_STYLES = 'styles'


	def docFolder = project.file(DOCS)
	def scriptsFolder = project.file('scripts')
	def imagesFolder = project.file('images')
	def stylesFolder = project.file('styles')
	def templatesFolder = project.file('templates')
	def outputDir = project.file(new File(project.buildDir,'site'))
	def tmpFolder = project.file('tmp')
	def tmpTemplatesFolder = project.file(new File('tmp' , 'templates'))
	def docTypeNames = getTemplates(project)
	
	def docTypes = indexDocsPerType(project)


	/**
	 * Get just the name of the file minus the path and extension.
	 *
	 * @param file
	 */
	static fileBaseName(file) {

		return file.name.replaceFirst(~/\.[^\.]+$/, '')
	}

	static getTemplates(project) {
		def docTypeNames    = [] as Set
		def docFolder = project.file(DOCS)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypeNames.add(docFile.parentFile.name)
		}
		return docTypeNames
	}

	static indexDocsPerType(project) {
		def docTypes        = [:]
		def docFolder = project.file(DOCS)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypes.put(fileBaseName(docFile), docFile.parentFile.name)
		}
		return docTypes
	}
}
