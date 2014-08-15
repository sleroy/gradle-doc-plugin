package com.metrixware.gradle.markdown

import java.text.SimpleDateFormat

import org.gradle.api.DefaultTask

class DocumentationTask extends DefaultTask {

	def scriptsFolder = project.file(project.documentation.folder_scripts)
	def imagesFolder = project.file(project.documentation.folder_images)
	def stylesFolder = project.file(project.documentation.folder_styles)
	def templatesFolder = project.file(project.documentation.folder_templates)
	def outputDir = project.file(new File(project.buildDir,project.documentation.folder_output))
	def tmpFolder = project.file(project.documentation.folder_tmp)
	def tmpTemplatesFolder = project.file(new File(project.documentation.folder_tmp , 'templates'))
	// Preprocess the template files to insert the correct document date and project version
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
		def docFolder = project.file(project.documentation.folder_docs)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypeNames.add(docFile.parentFile.name)
		}
		return docTypeNames
	}

	static indexDocsPerType(project) {
		def docTypes        = [:]
		def docFolder = project.file(project.documentation.folder_docs)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypes.put(fileBaseName(docFile), docFile.parentFile.name)
		}
		return docTypes
	}
}
