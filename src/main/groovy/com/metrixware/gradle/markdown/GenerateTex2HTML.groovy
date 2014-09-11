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

import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.markdown.postprocess.Latex2HtmlReferencesPostprocessor

/**
 * Generates the documentation in html format
 * @author sleroy
 *
 */
class GenerateTex2HTML extends DocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('tex-html')

	/**
	 * This task collects all md files into the tmp folder and converts them into html files, and then copy resources required to display the pictures
	 */
	@TaskAction
	void runTask() {

		project.fileTree(tmpFolder) { include '**/*.tex' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def tmp = docTypes
			def docType     = tmp.get(docFileBase)
			def conversion  =project.documentation.conversions[docType]
			if (conversion!=null && conversion.contains('html')) {
				def output =  project.file("${outputDir}/${docType}/${docFileBase}.html")
				println "Generating HTML doc for ${docFileBase}..."
				println output
				def generateCmdLine = [
					project.documentation.panDocBin,
					'--write=html5',
					'--template=' + project.file("${tmpTemplatesFolder}/${docType}.html"),
					'--toc',
					'--toc-depth=2',
					'--section-divs',
					'--no-highlight',
					'--smart'
				]
				for (myVar in project.documentation.templateVariables) {
					generateCmdLine.add("--variable=${myVar.key}:${myVar.value}")
				}
				generateCmdLine.addAll( [
					'--output=' + project.file("${outputDir}/${docType}/${docFileBase}.html"),
					"${docFile}"
				])
				project.exec({
					commandLine = generateCmdLine
					workingDir = tmpFolder
				}
				)
				def postprocess = new Latex2HtmlReferencesPostprocessor("utf-8")
				postprocess.process(output)
			}
		}
		LOGGER.info('Copying resources(pic, scripts, styles) files into distribution site')
		// Copy over resources needed for the HTML docs
		copyGeneratedAndCompiledResources()
	}

	private copyGeneratedAndCompiledResources() {
		project.copy {
			from(tmpFolder) {
				include 'images/**'
				include 'scripts/**'
				include 'styles/**'
			}
			into outputDir
		}
		for (String docType in docTypeNames) {
			project.copy {
				from(new File(tmpFolder, docType)) {
					include 'images/**'
					include 'scripts/**'
					include 'styles/**'
				}
				into new File(outputDir, docType)
			}
		}
	}
}


