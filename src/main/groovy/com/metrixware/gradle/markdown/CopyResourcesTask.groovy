/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.markdown

import java.text.SimpleDateFormat

import static com.metrixware.gradle.markdown.Utils.*
import nz.net.ultraq.lesscss.LessCSSCompiler

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CopyResourcesTask extends DefaultTask {

	static final LESS_EXTENSION = '**/*.less'
	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-copy')


	@TaskAction
	void runTask() {
		def docFolder = project.file(project.documentation.folder_docs)
		def scriptsFolder = project.file(project.documentation.folder_scripts)
		def stylesFolder = project.file(project.documentation.folder_styles)
		def templatesFolder = project.file(project.documentation.folder_templates)
		def outputDir = project.file(project.buildDir.path + '/' +  project.documentation.folder_output)
		def outputDirDoc = project.file(project.buildDir.path + '/' +  project.documentation.folder_outputdoc)
		def tmpFolder = project.file(project.documentation.folder_tmp)
		def tmpTemplatesFolder = project.file(project.documentation.folder_tmp + '/templates')


		def docTypeNames = getTemplates(project)

		

		LOGGER.debug("Creating temporary folder in $tmpFolder")
		
		tmpFolder.mkdirs()
		LOGGER.debug("Creating temporary templates folder in $tmpTemplatesFolder")
		tmpTemplatesFolder.mkdirs()
		LOGGER.debug('Creating output directory')
		outputDir.mkdirs()
		LOGGER.debug('Creating output documentation directory')
		outputDirDoc.mkdirs()

		LOGGER.info("Document templates found : $docTypeNames")

		LOGGER.info('Copy all md resources into a same folder')
		// Copy all .md files into the same directory
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			project.copy {
				from docFile
				into tmpFolder
			}
		}


		LOGGER.info('Copying pictures...')
		docTypeNames.each { docTypeName ->
			def imagesDir = project.file("${docFolder}/${docTypeName}/images")
			project.copy {
				from imagesDir
				into "${tmpFolder}/images"
			}
		}
		LOGGER.info('Copying JS scripts...')
		// Copy all resource directories straight over
		project.copy {
			from scriptsFolder
			into "${tmpFolder.path}/${scriptsFolder.name}"
		}

		LOGGER.info('Copying CSS and compiling LESS files before copy...')
		// Copy over stylesheets, compiling LessCSS files into CSS
		project.copy {
			from(stylesFolder) { exclude LESS_EXTENSION }
			into "${tmpFolder.path}/styles"
		}
		LessCSSCompiler compiler = new LessCSSCompiler()
		project.fileTree(scriptsFolder) { include LESS_EXTENSION}.each { lessFile ->
			def lessFileBase = fileBaseName(lessFile)
			compiler.compile(lessFile, project.file("${tmpFolder.path}/styles/${lessFileBase}.css"))
		}
		LOGGER.info('Preprocessing templates...')
		// Preprocess the template files to insert the correct document date and project version
		def documentVersion = new SimpleDateFormat('yyyyMMdd - dd MMMM yyyy', Locale.ENGLISH).format(new Date())

		def magicVariablesMap = new Properties()
		properties['documentVersion']=  documentVersion
		properties['projectVersion']=  project.version
		properties.putAll(project.documentation.variables)

		project.copy {
			from(templatesFolder) {
				filter(ReplaceTokens, tokens: magicVariablesMap)
			}
			into tmpTemplatesFolder
		}



	}
}
