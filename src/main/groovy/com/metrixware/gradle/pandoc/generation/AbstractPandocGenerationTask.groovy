package com.metrixware.gradle.pandoc.generation

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension

abstract class AbstractPandocGenerationTask extends AbstractGenerationTask{
	protected static final Logger LOGGER = LoggerFactory.getLogger('pandoc-generation')
	
	@Override
	protected void process(File folder, DocumentExtension doc,
			TemplateExtension template, String lang, String output) {
		def tmpOutDir = getTempOutputFolder(doc,template,lang,output)
		def input = getTempSourceDocument(doc, template, lang, output)
		def tmpOut = getTempOutputDocument(doc, template, lang, output)
		def generateCmdLine = [
			project.documentation.panDocBin,
			'--write='+getPandocOutput(output),
			'--toc',
			'--toc-depth='+tocDepth,
			'--section-divs',
			'--smart',
			'--output=' + tmpOut,
			input
		]
		if(hasTemplate(template,output)){
			generateCmdLine.add('--template=' + project.file("$tmpOutDir/template.tpl"))
		}else{
			generateCmdLine.add('--standalone')
		}	
		LOGGER.info('Execute cmd '+StringUtils.join(generateCmdLine, ' '))
		project.exec({
			commandLine = generateCmdLine
			workingDir = getTempOutputFolder(doc, template, lang, output)
		})

		copyToSite(folder, doc, template, lang, output)
	}
			
	protected int getTocDepth(){
		return 3	
	}
			
	protected String getPandocOutput(String output){
		return output
	}

	protected void copyToSite(File folder, DocumentExtension doc, TemplateExtension template,String lang, String output) {
		def tmpOut = getTempOutputDocument(doc, template, lang, output)
		def out =  getDocumentOutputFile(doc, template, lang, output)
		LOGGER.info("Copy ${folder} into ${out}")
		out.parentFile.mkdirs()
		FileUtils.copyFile(tmpOut,out)
	}
}