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
package com.metrixware.gradle.pandoc

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

import com.metrixware.gradle.pandoc.generation.EclipseGenerationTask
import com.metrixware.gradle.pandoc.generation.EpubGenerationTask
import com.metrixware.gradle.pandoc.generation.HtmlGenerationTask
import com.metrixware.gradle.pandoc.generation.Md2PdfGenerationTask
import com.metrixware.gradle.pandoc.generation.Tex2PdfGenerationTask
import com.metrixware.gradle.pandoc.project.CleanTask;
import com.metrixware.gradle.pandoc.project.DocumentationConfigurationTask
import com.metrixware.gradle.pandoc.project.DocumentationPrepareTask
import com.metrixware.gradle.pandoc.project.ToolCheckingTask




/**
 * @author Sylvain Leroy
 */
class PandocPlugin implements Plugin<Project> {
	private static final String DOCUMENTATION = 'Documentation'

	void apply(Project project) {
		ExtensionAware documentation = project.extensions.create('documentation', Documentation)
		documentation.extensions.templates = project.container(Template)
		documentation.extensions.documents = project.container(Document)
		documentation.extensions.repositories = project.container(Repository)

		project.task('pandoc-configure',
		type: DocumentationConfigurationTask,
		group: DOCUMENTATION,
		description: 'Initializes the project structure directories from the templates definition.')

		project.task('pandoc-prepare',
		type: DocumentationPrepareTask,
		group: DOCUMENTATION,
		dependsOn: ['pandoc-configure'],
		description: 'Copy the documentation resources into the temporary folder to produce the documentation.')

		project.task('pandoc-clean',
		type: CleanTask,
		group: DOCUMENTATION,
		description: 'Clean-up the temporary and output directories. May be usefull to get the last version of a templates repository.')


		project.task('pandoc-check',
		type: ToolCheckingTask,
		group: DOCUMENTATION,
		description: 'Check if all required tools are installed.')


		project.task('pandoc-html',
		type: HtmlGenerationTask,
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-prepare',
			'pandoc-check'
		],
		description: 'Produce all HTML documentations.')

		project.task('pandoc-epub',
		type: EpubGenerationTask,
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-prepare',
			'pandoc-check'
		],
		description: 'Produce all EBooks documentations.')


		project.task('pandoc-tex2pdf',
		type: Tex2PdfGenerationTask,
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-prepare',
			'pandoc-check'
		],
		description: 'Produce all PDF documentations from LaTeX sources.')

		project.task('pandoc-md2pdf',
		type: Md2PdfGenerationTask,
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-prepare',
			'pandoc-check'
		],
		description: 'Produce all PDF documentations from markdown sources.')

		project.task('pandoc-pdf',
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-tex2pdf',
			'pandoc-md2pdf'
		],
		description: 'Produce all PDF documentations.')

		project.task('pandoc-eclipse',
		group: DOCUMENTATION,
		type: EclipseGenerationTask,
		dependsOn: [
			'pandoc-prepare',
			'pandoc-check'
		],
		description: 'Produce all Eclipse documentations.')

		project.task('pandoc-all',
		group: DOCUMENTATION,
		dependsOn: [
			'pandoc-pdf',
			'pandoc-epub',
			'pandoc-html',
			'pandoc-eclipse'
		],
		description: 'Produce all documentations.')
	}
}

