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

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class InitDocProjectTask extends DefaultTask {
	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-init')

	@TaskAction
	void runTask() {
		def docFolder = project.file(project.documentation.folder_docs)
		def scriptsFolder = project.file(project.documentation.folder_scripts)
		def stylesFolder = project.file(project.documentation.folder_styles)
		def templatesFolder = project.file(project.documentation.folder_templates)
		def folderTmp = project.file(project.documentation.folder_tmp)

		LOGGER.info('Creating documentation folder...')
		if (!docFolder.exists() && !docFolder.mkdirs()) {
			LOGGER.error("Could not create the folder $docFolder")
		}
		LOGGER.info('Creating scripts folder...')
		if (!scriptsFolder.exists() && !scriptsFolder.mkdirs()) {
			LOGGER.error("Could not create the folder $scriptsFolder")
		}
		LOGGER.info('Creating styles folder...')
		if (!stylesFolder.exists() && !stylesFolder.mkdirs()) {
			LOGGER.error("Could not create the folder $stylesFolder")
		}
		LOGGER.info('Creating templates folder...')
		if (!templatesFolder.exists() && !templatesFolder.mkdirs()) {
			LOGGER.error("Could not create the folder $templatesFolder")
		}
		LOGGER.info('Creating temporary folder...')
		if (!folderTmp.exists() && !folderTmp.mkdirs()) {
			LOGGER.error("Could not create the folder $folderTmp")
		}
		for (String key : project.documentation.conversions.keySet()) {
			def docTempl = project.file(docFolder.path +'/' + key)
			if (!docTempl.exists() && !docTempl.mkdirs()) {
				LOGGER.error("Could not create the folder $docTempl")
			}
			project.file(docFolder.path +'/' + key +'/' + project.documentation.folder_styles).mkdirs()
			project.file(docFolder.path +'/' + key +'/' + project.documentation.folder_scripts).mkdirs()
			project.file(docFolder.path +'/' + key +'/images').mkdirs()
			
		}
	}
}