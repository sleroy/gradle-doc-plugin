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

import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

import com.metrixware.gradle.pandoc.DocumentationTask;
/**
 * Generates the documentation in pdf format
 * @author sleroy
 *
 */
class GenerateHtml2PDF extends DocumentationTask {

	/**
	 * This task collects all html files generated inside the site/ folder and converts them into pdf.
	 */
	@TaskAction
	void runTask() {


		//		// Copy over the HTML documents into a directory from which we can host them
		//		// using the Jetty server
		//		project.copy {
		//			from outputDirDoc
		//			into outputDir
		//		}

		// Modify the copied HTML docs, removing the Google Analytics script as it
		// blocks the wkhtmltopdf process
		project.fileTree(outputDir) { include '*.html' }.each { docFile ->
			ant.replaceregexp(
					file:    docFile,
					match:   '<!-- Google Analytics script -->.*</script>',
					replace: '',
					flags:   'gis'
					)
		}
		// Generate the PDF documents from the modified HTML documents
		project.fileTree(outputDir) { include '**/*.html' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)

			if (project.documentation.conversions[docType] != null && project.documentation.conversions[docType].contains('pdf')) {
				println "Generating PDF doc for ${docFile}..."
				project.exec{
					commandLine = [
						project.documentation.wkhtmltopdfBin,
						'--print-media-type',
						'--dpi',
						"${project.documentation.pdfDpi}",
						'--margin-bottom',
						"${project.documentation.marginBottom}",
						'--footer-spacing',
						"${project.documentation.footerSpacing}",
						'--footer-font-size',
						"${project.documentation.footerFontSize}",
						'--footer-font-name',
						"'${project.documentation.footerFont}'",
						'--footer-right',
						"'${project.documentation.footerRightText}'",
						'--header-font-name',
						"'${project.documentation.headerFont}'",
						"${docFile.path}",
						project.file("${outputDir}/${docType}/doc/${docFileBase}.pdf")
					]
					workingDir = tmpFolder
				}
			}
		}

	}
}

