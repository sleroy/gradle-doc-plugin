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
package com.metrixware.gradle.pandoc.generation

import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension
import com.metrixware.gradle.pandoc.eclipse.EclipseDocumentationProjectGenerator
import com.metrixware.gradle.pandoc.eclipse.HtmlSectionExtractor

/**
 * Generates the documentation in HTML format from tex sources.
 *
 */
class EclipseGenerationTask extends HtmlGenerationTask {



	@Override
	protected boolean isSupported(DocumentExtension doc, String output) {
		return 'eclipse'.equals(output) && ('tex'.equals(doc.type)||'md'.equals(doc.type))
	}
	
	
	@Override
	protected void copyToSite(File folder, DocumentExtension doc,
			TemplateExtension template, String lang, String output) {
			
		super.copyToSite(folder, doc, template, lang, output)
		
		//initialize the project generator	
		def variables  = getDocumentVariables(doc, lang)	
		def projectgenerator = new EclipseDocumentationProjectGenerator()	
		projectgenerator.lang=lang
		projectgenerator.name=variables.get('eclipse.name')
		if(projectgenerator.name==null || projectgenerator.name.isEmpty()){
			throw new IllegalStateException('eclipse.name property has not be filled. Unable to generate Eclipse project')
		}
		projectgenerator.project =variables.get('eclipse.project')
		if(projectgenerator.project==null || projectgenerator.project.isEmpty()){
			throw new IllegalStateException('eclipse.project property has not be filled. Unable to generate Eclipse project')
		}
		projectgenerator.vendor =variables.get('eclipse.vendor')
		projectgenerator.version =variables.get('eclipse.version')
		if(projectgenerator.version==null || projectgenerator.version.isEmpty()){
			throw new IllegalStateException('eclipse.version property has not be filled. Unable to generate Eclipse project')
		}
		
		//generate eclipse project and its toc.xml
		def tempOutFolder = getTempOutputFolder(doc, template, lang, output)
		def outFolder = getDocumentOutputFolder(doc, template, lang, output)
		def outFile = getDocumentOutputFile(doc, template, lang, output)
		def toc = HtmlSectionExtractor.extract(outFile.text, 3)
		projectgenerator.generate(tempOutFolder, toc,'html/${doc.name}-${lang}.html')
		def eclipseFolder = projectgenerator.getProjectDirectory(tempOutFolder)

		FileUtils.moveDirectory(outFolder, FileUtils.getFile(eclipseFolder,'html'))
		FileUtils.moveDirectory(eclipseFolder, FileUtils.getFile(outFolder,eclipseFolder.name))
	}
			
	 File getTempOutputDocument(DocumentExtension doc, TemplateExtension template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, '${doc.name}-${lang}.html')
	}
	
	 File getDocumentOutputFile(DocumentExtension doc, TemplateExtension template, String lang, String output){
		return FileUtils.getFile(getDocumentOutputFolder(doc,template,lang,output),'${doc.name}-${lang}.html')
	}

}


