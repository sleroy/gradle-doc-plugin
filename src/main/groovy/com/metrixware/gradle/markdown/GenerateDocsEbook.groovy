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

/**
 * Generates the documentation in ebook format
 * @author sleroy
 *
 */
class GenerateDocsEbook extends DefaultTask{
	
	
	@TaskAction
	void runTask() {
		def docTypes = indexDocsPerType(project)

		def outputDirDoc = project.file(project.documentation.folder_outputdoc)
		def tmpFolder = project.file(project.documentation.folder_tmp)
		def tmpTemplatesFolder = project.file(project.documentation.folder_tmp + '/templates')

		// Currently only limited to tutorials
		project.fileTree(tmpFolder) { include '**/*.md' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)

			if (project.documentation.conversions[docType].contains('ebook')) {
				println 'Generating E-books for ${docFileBase}...'
				project.exec{
					commandLine=[
						'pandoc',
						'--write=epub',
						'--template=' + project.file("${tmpTemplatesFolder}/${docType}.epub"),
						'--toc',
						'--toc-depth=4',
						'--section-divs',
						'--smart',
						'--output=' + project.file("${outputDirDoc}/${docFileBase}.epub"),
						"${docFile}"					
					]
					workingDir = tmpFolder
					}
				project.exec{commandLine=[
					'ebook-convert',
					project.file("${outputDirDoc}/${docFileBase}.epub"),
					project.file("${outputDirDoc}/${docFileBase}.mobi")
				]}
			}
		}
	}
}
