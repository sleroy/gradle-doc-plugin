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
package com.metrixware.gradle.pandoc.generation

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.StrSubstitutor
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.common.collect.Maps
import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension

/**
 * Generates the documentation in PDF format from tex sources. 
 * It use pdflatex to perform the conversion.
 *
 */
class Tex2PdfGenerationTask extends AbstractGenerationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-tex2pdf')


	@Override
	protected boolean isSupported(DocumentExtension doc, String output) {
		return 'pdf'.equals(output) && 'tex'.equals(doc.type)
	}

	@Override
	protected void process(File folder, DocumentExtension doc,
			TemplateExtension template, String lang, String output) {
		def tmpOutDir = getTempOutputFolder(doc,template,lang,output)
		def input = getTempSourceDocument(doc, template, lang, output)
		def tmpOut = getTempOutputDocument(doc, template, lang, output)
		injectTemplateInDocument(input,getTempTemplateFile(doc, template, lang, output))

		def generateCmdLine = [
			project.documentation.pdfTexBin,
			'-file-line-error',
			'-interaction=nonstopmode',
			input
		]
		LOGGER.info('Execute cmd '+StringUtils.join(generateCmdLine, ' '))
		project.exec({
			commandLine = generateCmdLine
			workingDir = tmpOutDir
		})
		//2nd step for tables of contents
		project.exec({
			commandLine = generateCmdLine
			workingDir = tmpOutDir
		})


		def out =  getDocumentOutputFile(doc, template, lang, output)

		LOGGER.info("Copy $tmpOut into $out")
		out.parentFile.mkdirs()
		FileUtils.copyFile(tmpOut,out)
	}

	/**
	 * Inject the document in the template.		
	 * @param document
	 * @param template
	 */
	private void injectTemplateInDocument(File document, File template){
		def templateContent =template.text
		if(!templateContent.isEmpty()){
			Map<String, String> map = new HashMap<String, String>()
			map.put('body', document.text)
			StrSubstitutor replacer = new StrSubstitutor(map)
			replacer.setVariablePrefix('$')
			replacer.setVariableSuffix('$')

			String replaced = replacer.replace(templateContent)
			FileUtils.write(document, replaced)
		}
	}
}


