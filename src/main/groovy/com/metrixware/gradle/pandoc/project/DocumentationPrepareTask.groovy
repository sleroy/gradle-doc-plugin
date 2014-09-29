/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License')
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
package com.metrixware.gradle.pandoc.project



import javax.activation.MimetypesFileTypeMap
import javax.imageio.ImageIO

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.AbstractDocumentationTask
import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Repository;
import com.metrixware.gradle.pandoc.Template

class DocumentationPrepareTask extends AbstractDocumentationTask {


	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-prepare')

	protected void process() {
		print('Prepare the documentation folders')

		initFolders()

		LOGGER.info('Fetching repositories into temporary directory...')
		for(Repository repo : repositories){
			fetchIfNeeded(repo)
		}

		LOGGER.info('Copy templates into temporary directory...')
		FileUtils.copyDirectory(templatesFolder, tmpTemplatesFolder)

		def magicVariablesMap = globalVariables

		project.fileTree(tmpTemplatesFolder) { include '**/*.tpl' }.each { docFile ->
			LOGGER.info('-- Inject global variables in template file '+docFile)
			preprocess(docFile,magicVariablesMap)
		}


		LOGGER.info('Copy sources for each supported template...')
		for(Document document : documents){
			def supported = templates.findAll { Template t ->
				document.support(t)
			}
			for(Template template : supported){
				for(String output : template.outputs){
					for(String lang : document.languages){
						injectTemplate(document, template, lang, output, magicVariablesMap)
					}
				}
			}
		}
	}



	private fetchIfNeeded(Repository repository){
		try{
			URL url = new URL(repository.url)

			def out = FileUtils.getFile(tmpTemplatesFolder,FilenameUtils.getName(url));
			if(!out.exists()){
				LOGGER.info("-- ${repository.url} : downloading templates.")
				FileUtils.copyURLToFile(url, out)
				ZipFile zipFile = new ZipFile(out)
				try{
					zipFile.extractAll(tmpTemplatesFolder)
				}catch(ZipException e){
					LOGGER.error("Unable to unzip template repository ${repository.url}")
				}
			}else{
				LOGGER.info("-- ${repository.url} : already downloaded.")
			}
		}catch( e){
			LOGGER.error("Unable to fetch repository ${repository.url}", e)
		}
	}

	private injectTemplate(Document document, Template template, String lang, String output, Properties magicVariablesMap) {
		def dir = getTempOutputFolder(document, template, lang, output)

		LOGGER.info('-- Prepare document '+document.name+' '+template.name+' ['+output+','+lang+'] in '+dir )
		Properties docVariables = getDocumentVariables(document, lang)
		docVariables.putAll(magicVariablesMap)
		dir.mkdirs()

		//copy document sources into tmp folder for the tuple (template,lang,output)
		project.copy {
			from getDocumentFolder(document,lang)
			into dir
		}

		//copy template into document folder
		project.copy {
			from getTempTemplateFolder(template,output)
			into dir
		}
		//injecting variables into each text file (excluding .properties)
		File tempOutput = getTempOutputFolder(document, template, lang, output)
		project.fileTree(tempOutput).each { File file ->
			if(isSourceFile(file)){

				LOGGER.info('--- Inject document variables in text file '+file  )
				preprocess(file,docVariables)

			}
		}
	}

	protected boolean isSourceFile(File file){
		String[] types = project.documentation.sources
		for(String type: types){
			if(file.name.endsWith(type)){
				return true
			}
		}
		return false
	}




	private initFolders() {
		LOGGER.debug('Creating temporary folder in ${tmpFolder}')
		FileUtils.deleteDirectory(tmpFolder)
		tmpFolder.mkdirs()
		LOGGER.debug('Creating temporary templates folder in ${tmpTemplatesFolder}')
		tmpTemplatesFolder.mkdirs()
		LOGGER.debug('Creating output directory')
		FileUtils.deleteDirectory(outputDir)
		outputDir.mkdirs()
		LOGGER.debug('Creating output documentation directory')
	}

}
