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

import static com.metrixware.gradle.markdown.Utils.*

import java.text.SimpleDateFormat

import nz.net.ultraq.lesscss.LessCSSCompiler

import org.apache.commons.io.FileUtils
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CopyResourcesTask extends DocumentationTask {

	static final LESS_EXTENSION = '**/*.less'
	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-copy')

	void copyGlobalResources() {
		LOGGER.info('Copying global pictures...')
		project.copy {
			from project.file(project.documentation.folder_images)
			into "${tmpFolder.path}/${imagesFolder.name}"
		}

		LOGGER.info('Copying global JS scripts...')
		project.copy {
			from project.file(project.documentation.folder_scripts)
			into "${tmpFolder.path}/${scriptsFolder.name}"
		}

		LOGGER.info('Copying global CSS ...')
		project.copy {
			from(stylesFolder) { exclude LESS_EXTENSION }
			into "${tmpFolder.path}/styles"
		}

		LOGGER.info('Copying global LESS ...')
		LessCSSCompiler compiler = new LessCSSCompiler()
		project.fileTree(stylesFolder) { include LESS_EXTENSION}.each { lessFile ->
			def lessFileBase = fileBaseName(lessFile)
			compiler.compile(lessFile, project.file("${tmpFolder.path}/styles/${lessFileBase}.css"))
		}
	}

	void copyMarkdownFiles() {
		LOGGER.info('Copy all md resources into a same folder')
		// Copy all .md files into the same directory
		docTypeNames.each { docTypeName ->
			def mdOutputDir = new File(tmpFolder+ '/' + docTypeName+'/')
			project.fileTree(docFolder + '/' + docTypeName) { include '**/*.md' }.each { docFile ->
				project.copy {
					from(docFile) {
						filter(ReplaceTokens, tokens: magicVariablesMap)
					}
					into mdOutputDir
				}
			}
		}

	}

	void copyPerTemplateResources() {

		LOGGER.info('Copying per-template resources...')

		docTypeNames.each { docTypeName ->
			def imagesDir = project.file("${docFolder}/${docTypeName}/images")
			def imageOutputDir = new File(tmpFolder+ '/' + docTypeName+'/images')
			local.mkdirs()
			project.copy {
				from imagesDir
				into imageOutputDir
			}
			def cssDir = project.file("${docFolder}/${docTypeName}/css")
			def cssOutputDir = new File(tmpFolder+ '/' + docTypeName+'/css')
			local.mkdirs()
			project.copy {
				from cssDir
				into cssOutputDir
			}
			def scriptDir= project.file("${docFolder}/${docTypeName}/scripts")
			def scriptOutputDir = new File(tmpFolder+ '/' + docTypeName+'/scripts')
			local.mkdirs()
			project.copy {
				from scriptDir
				into scriptOutputDir
			}
		}


	}

	@TaskAction
	void runTask() {


		initFolders()

		LOGGER.info("Document templates found : $docTypeNames")


		// Copy global resource
		copyGlobalResources()

		// Copy template resources
		copyPerTemplateResources()

		// Copy markdown files
		copyMarkdownFiles()

		// Preprocess templates
		preprocessTemplates()


	}

	void preprocessTemplates() {


		LOGGER.info('Preprocessing templates...')

		project.copy {
			from(templatesFolder) {
				filter(ReplaceTokens, tokens: magicVariablesMap)
			}
			into tmpTemplatesFolder
		}

	}

	private initFolders() {
		LOGGER.debug("Creating temporary folder in $tmpFolder")
		FileUtils.deleteDirectory(tmpFolder)
		tmpFolder.mkdirs()
		LOGGER.debug("Creating temporary templates folder in $tmpTemplatesFolder")
		tmpTemplatesFolder.mkdirs()
		LOGGER.debug('Creating output directory')
		outputDir.mkdirs()
		LOGGER.debug('Creating output documentation directory')
		outputDirDoc.mkdirs()

		docTypeNames.each { docTypeName ->
			new File(outputDir, docTypeName).mkdirs()
			new File(outputDirDoc, docTypeName).mkdirs()
		}
	}


}
