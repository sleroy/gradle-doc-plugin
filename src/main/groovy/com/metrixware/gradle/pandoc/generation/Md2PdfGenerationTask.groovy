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

import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger

import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Template



class Md2PdfGenerationTask extends AbstractPandocGenerationTask {


	@Override
	protected boolean isSupported(Document doc, String output) {
		return 'pdf'.equals(output) && ('md'.equals(doc.type))
	}

	@Override
	protected void process(File folder, Document doc,
			Template template, String lang, String output) {
		def tmpOutDir = getTempOutputFolder(doc,template,lang,output)
		def input = getTempSourceDocument(doc, template, lang, output).name
		def tmpOut = getTempOutputDocument(doc, template, lang, output).name


		def generateCmdLine = [
			project.documentation.panDocBin,
			'--toc',
			'--toc-depth='+tocDepth,
			'--section-divs',
			'--smart',
			'--listings',
			'--latex-engine=xelatex',
			'--output' ,
			tmpOut
		]
		if(hasTemplate(template,output)){
			generateCmdLine.add('--template=template.tpl')
		}else{
			generateCmdLine.add('--standalone')
		}

		generateCmdLine.add(input)

		LOGGER.info('Working dir '+tmpOutDir)
		LOGGER.info('Execute cmd '+StringUtils.join(generateCmdLine, ' '))
		project.exec({
			commandLine = generateCmdLine
			workingDir = tmpOutDir
		})

		copyToSite(folder, doc, template, lang, output)
	}
}


