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

import com.google.common.base.Strings;
import com.metrixware.gradle.markdown.postprocess.Latex2HtmlReferencesPostprocessor

/**
 * Generates the documentation in html format
 * @author sleroy
 *
 */
class GenerateTex2Pdf extends DocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('tex-pdf')

	/**
	 * This task collects all md files into the tmp folder and converts them into html files, and then copy resources required to display the pictures
	 */
	@TaskAction
	void runTask() {

		project.fileTree(tmpFolder) { include '**/*.tex' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def tmp = docTypes
			def docType     = tmp.get(docFileBase)
			def pdf = docFile.name.replace(".tex", ".pdf")
			def conversion  =project.documentation.conversions[docType]
			if (conversion!=null && conversion.contains('pdf')) {
				def output =  project.file("${outputDir}/${docType}/${docFileBase}.pdf")
				println "Generating PDF doc for ${docFileBase}..."
				println output
				def generateCmdLine = [
					project.documentation.pdfTexBin,
					'-file-line-error',
					'-interaction=nonstopmode',
					docFile
				]
				println "Execute "+generateCmdLine
				println "in ${tmpFolder}/${docType}"
				project.exec({
					commandLine = generateCmdLine
					workingDir = "${tmpFolder}/${docType}"
				}
				)
				project.copy {
					from("${tmpFolder}/${docType}") {
						include pdf
					}
					into "${outputDir}/${docType}"
				}
			}
		}
		
	
	}
	


}


