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

import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.exception.ZipException

import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.collect.Lists
import com.metrixware.gradle.pandoc.AbstractDocumentationTask
import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Repository
import com.metrixware.gradle.pandoc.Template

class DocumentationPrepareTask extends AbstractDocumentationTask {


	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-prepare')

	protected void process() {
		println('Prepare the documentation folders')

		initFolders()

		def repos = tempRepositoriesDirectory()

		LOGGER.debug("Fetching repositories into ${repos}")
		for(Repository repo : repositories){
			fetchIfNeeded(repo,repos)
		}

		//add the templates downloaded from repositories
		for(File remoteTemplate : repos.listFiles()){
			if(remoteTemplate.isDirectory()){
				def scanned = scan(remoteTemplate)
				if(templates.find{Template t -> t.name.equals(scanned.name)}==null){
					project.documentation.templates.add(scanned)
					def remote = FileUtils.getFile(repos,scanned.name)
					LOGGER.debug("-- Add template ${scanned.name} from remote repositories.")
					FileUtils.copyDirectoryToDirectory(remote, tmpTemplatesFolder)
				}else{
					LOGGER.warn("Template ${scanned.name} already exists in the documentation project, remote template will be ignored.")
				}
			}
		}


		LOGGER.debug('Copy templates into temporary directory')
		FileUtils.copyDirectory(templatesFolder, tmpTemplatesFolder)

		def magicVariablesMap = globalVariables

		project.fileTree(tmpTemplatesFolder) { include '**/*.tpl' }.each { docFile ->
			LOGGER.debug('Inject global variables in template file '+docFile)
			preprocess(docFile,magicVariablesMap)
		}


		LOGGER.debug("Prepare documents for templates ${templates}")
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

	private File tempRepositoriesDirectory(){
		return FileUtils.getFile(tmpFolder,'repositories')
	}


	private fetchIfNeeded(Repository repository, File tmpDir){
		try{
			URL url = new URL(repository.url)
			def name = FilenameUtils.getName(url.toString())
			def out = FileUtils.getFile(tmpDir,name)
			if(!out.exists()){
				LOGGER.info("Download repository ${repository.url}")
				FileUtils.copyURLToFile(url, out)
				ZipFile zipFile = new ZipFile(out)
				try{
					zipFile.extractAll(tmpDir.canonicalPath)
				}catch(ZipException e){
					LOGGER.error("Unable to unzip template repository ${repository.url}")
				}
			}else{
				LOGGER.info("Repository ${repository.url} already in cache")
			}
		}catch( e){
			LOGGER.error("Unable to fetch repository ${repository.name}", e)
		}
	}

	private Template scan(File directory){
		Template template = new Template(directory.name)
		List<String> outputs = new ArrayList<String>()
		for(File output : directory.listFiles()){
			outputs.add(output.name)
		}
		template.setOutputs(outputs.toArray(new String[outputs.size()]))
		return template
	}

	private injectTemplate(Document document, Template template, String lang, String output, Properties magicVariablesMap) {
		def dir = getTempOutputFolder(document, template, lang, output)

		LOGGER.debug('-- Prepare document '+document.name+' '+template.name+' ['+output+','+lang+'] in '+dir )
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

				LOGGER.debug('--- Inject document variables in text file '+file  )
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
		LOGGER.debug("Creating temporary templates directory in ${tmpTemplatesFolder}")
		tmpTemplatesFolder.deleteDir()
		tmpTemplatesFolder.mkdirs()
		LOGGER.debug("Creating output directory in ${outputDir}")
		FileUtils.deleteDirectory(outputDir)
		outputDir.mkdirs()
	}

}
