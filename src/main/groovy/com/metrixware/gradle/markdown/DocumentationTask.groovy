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
	def magicVariablesMap = preparingMagicVariables()
	def docTypes = indexDocsPerType(project)

	private Properties preparingMagicVariables() {
		def documentVersion = new SimpleDateFormat('yyyyMMdd - dd MMMM yyyy', Locale.ENGLISH).format(new Date())
		def magicVariablesMap = new Properties()
		magicVariablesMap['documentVersion']=  documentVersion.toString()
		magicVariablesMap['date']=  new Date().toString()
		magicVariablesMap['projectVersion']=  project.version.toString()
		magicVariablesMap.putAll(project.documentation.templateVariables)
		return magicVariablesMap
	}

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
