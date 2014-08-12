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
 * Generates the documentation in html format
 * @author sleroy
 *
 */
class GenerateDocsHTML extends DefaultTask {
	
	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-html')
	
	
	@TaskAction
	void runTask() {
		def docTypeNames = getTemplates(project)
		def docTypes = indexDocsPerType(project)

		def outputDir = project.file(project.documentation.folder_output)
		def outputDirDoc = project.file(project.documentation.folder_outputdoc)
		def tmpFolder = project.file(project.documentation.folder_tmp)
		def tmpTemplatesFolder = project.file(project.documentation.folder_tmp + '/templates')
		project.fileTree(tmpFolder) { include '**/*.md' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)

			if (project.documentation.conversions[docType].contains('html')) {
				println "Generating HTML doc for ${docFileBase}..."
				println project.file("${outputDirDoc}/${docFileBase}.html")
				project.exec({
					commandLine = [
						"pandoc",
						"--write=html5",
						"--template=" + project.file("${tmpTemplatesFolder}/${docType}.html"),
						"--toc",
						"--toc-depth=4",
						"--section-divs",
						"--no-highlight",
						"--smart",
						"--output=" + project.file("${outputDirDoc}/${docFileBase}.html"),
						"${docFile}"
					]
					workingDir = tmpFolder
				}
				)
			}
		}
		LOGGER.info("Moving generated files into distribution site")
		// Copy over resources needed for the HTML docs
		project.copy {
			from(tmpFolder) {
				include 'images/**'
				include 'scripts/**'
				include 'styles/**'
			}
			into outputDirDoc
		}
	}


}


