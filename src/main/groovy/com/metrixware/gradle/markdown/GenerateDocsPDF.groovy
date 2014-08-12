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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory
/**
 * Generates the documentation in pdf format
 * @author sleroy
 *
 */
class GenerateDocsPDF extends DefaultTask {
	
	
	@TaskAction
	void runTask() {
		def docTypes = indexDocsPerType(project)

		def outputDirDoc = project.file(project.documentation.folder_outputdoc)
		def tmpFolder = project.file(project.documentation.folder_tmp)

		def webAppDirName = outputDirDoc
		//		// Copy over the HTML documents into a directory from which we can host them
		//		// using the Jetty server
		//		project.copy {
		//			from outputDirDoc
		//			into webAppDirName
		//		}

		// Modify the copied HTML docs, removing the Google Analytics script as it
		// blocks the wkhtmltopdf process
		project.fileTree(webAppDirName) { include '*.html' }.each { docFile ->
			ant.replaceregexp(
					file:    docFile,
					match:   '<!-- Google Analytics script -->.*</script>',
					replace: '',
					flags:   'gis'
					)
		}

		// Generate the PDF documents from the modified HTML documents
		project.fileTree(webAppDirName) { include '*.html' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)

			if (project.documentation.conversions[docType].contains('pdf')) {
				println "Generating PDF doc for ${docFileBase}..."
				project.exec{
					commandLine = [
						'wkhtmltopdf',
						'--print-media-type',
						'--dpi',
						'150',
						'--margin-bottom',
						'15',
						'--footer-spacing',
						'5',
						'--footer-font-size',
						'8',
						'--footer-font-name',
						'\'Open Sans\'',
						'--footer-right',
						'Page [page] of [topage]',
						'--header-font-name',
						'\'Open Sans\'',
						"${buildDir}/jetty/${docFile.name}",
						project.file("${outputDirDoc}/${docFileBase}.pdf")
					]
					workingDir = tmpFolder
				}
			}
		}

	}
}
