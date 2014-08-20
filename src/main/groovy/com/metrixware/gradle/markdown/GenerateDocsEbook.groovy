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

import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction

/**
 * Generates the documentation in ebook format
 * @author sleroy
 *
 */
class GenerateDocsEbook extends DocumentationTask {

	/**
	 * This task converts markdown files from tmp folder into ebooks. 
	 */
	@TaskAction
	void runTask() {
		// Currently only limited to tutorials
		project.fileTree(tmpFolder) { include '**/*.md' }.each { docFile ->
			def docFileBase = fileBaseName(docFile)
			def docType     = docTypes.get(docFileBase)
			if (project.documentation.conversions[docType] == null) {
				throw new GradleScriptException("Markdown files found outside the docs folder, please verify $docFile", null)
			}
			if (project.documentation.conversions[docType].contains('ebook')) {
				println "Generating E-books for ${docFileBase}..."
				project.exec{
					commandLine=[
						'pandoc',
						'--write=epub',
						'--template=' + project.file("${tmpTemplatesFolder}/${docType}.epub"),
						'--toc',
						'--toc-depth=3',
						'--section-divs',
						'--smart',
						'--output=' + project.file("${outputDir}/${docType}/doc/${docFileBase}.epub"),
						"${docFile}"
					]
					workingDir = tmpFolder
				}
				project.exec{
					commandLine=[
						'ebook-convert',
						project.file("${outputDirDoc}/${docType}/${docFileBase}.epub"),
						project.file("${outputDirDoc}/${docType}/${docFileBase}.mobi")
					]}
			}
		}
	}
}
