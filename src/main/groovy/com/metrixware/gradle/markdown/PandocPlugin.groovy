/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.markdown

import org.gradle.api.Plugin
import org.gradle.api.Project




/**
 * @author Sylvain Leroy
 */
class PandocPlugin implements Plugin<Project> {
	private static final String DOCUMENTATION = 'Documentation'

	void apply(Project project) {
		project.extensions.create('documentation', DocumentationConfiguration)

		project.task('checkTools',
		type: MarkdownToolCheckingTask,
		group: DOCUMENTATION,
		description: 'Check that required tools are presents on the system')

		project.task('checkStructure',
		type: MarkdownStructureCheckingTask,
		group: DOCUMENTATION,
		description: 'Check that the project has the appropriate folder structure')


		project.task('initDocProject',
		type: InitDocProjectTask,
		group: DOCUMENTATION,
		description: 'Initializes the current project by creating the default project structure.')

		project.task('copyResources',
		type: CopyResourcesTask,
		group: DOCUMENTATION,
		dependsOn: [
			'initDocProject',
			'checkStructure'
		],
		description: 'Copy the documentation resources into the temporary folder to produce the documentation.')


		project.task('generateDocsHTML',
		type: GenerateDocsHTML,
		group: DOCUMENTATION,
		dependsOn: ['checkTools','copyResources'],
		description: 'Generates the documentation in HTML format.')
		
		project.task('generateDocsTex2HTML',
		type: GenerateTex2HTML,
		group: DOCUMENTATION,
		dependsOn: ['copyResources'],
		description: 'Generates the documentation in HTML format from a TeX input.')
		
		
		project.task('generateDocsTex2PDF',
		type: GenerateTex2Pdf,
		group: DOCUMENTATION,
		dependsOn: ['copyResources'],
		description: 'Generates the documentation in PDF format from a TeX input.')

		project.task('generateDocsEbook',
		type: GenerateDocsEbook,
		group: DOCUMENTATION,
		dependsOn: ['checkTools','copyResources'],
		description: 'Generates the documentation in Ebook format.')

		project.task('generateDocsPDF',
		type: GenerateDocsPDF,
		group: DOCUMENTATION,
		dependsOn: ['checkTools','copyResources'],
		description: 'Generates the documentation in PDF format.')

		project.task('generateDocs',
		dependsOn: [
			'generateDocsHTML',
			'generateDocsEbook',
			'generateDocsPDF'
		],
		group: DOCUMENTATION,
		description: 'Generate HTML, E-book and PDF documents from markdown docs')
	}
}

