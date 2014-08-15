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

import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Sylvain Leroy
 */
class MarkdownStructureCheckingTask extends DocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('markdown-env')

	@TaskAction
	void runTask() {
		if (project.documentation == null) {
			throw new GradleScriptException('Missing extension documentation')
		}
		def docFolder = project.file(project.documentation.folder_docs)
		def scriptsFolder = project.file(project.documentation.folder_scripts)
		def stylesFolder = project.file(project.documentation.folder_styles)
		def templatesFolder = project.file(project.documentation.folder_templates)
		//def outputSiteFolder = project.file(FolderConstants.OUTPUTSITE_FOLDER)
		def cond = true
		cond &= this.checkExisting('Documentation folder', docFolder)
		cond &= checkExisting('Scripts folder', scriptsFolder)
		cond &= checkExisting('CSS style folder', stylesFolder)
		cond &= checkExisting('Html templates', templatesFolder)
		if (!cond) {
			LOGGER.error('At least one folder is missing, please check the configuration of your project.')
			throw new GradleScriptException('could not generate markdown configuration, the project configuration is uncomplete', null)
		}
		LOGGER.info('Checks the template folders in docs')

		for (String key : project.documentation.conversions.keySet()) {
			def tpl = project.file(docFolder.path +'/' + key)
			if (!tpl.exists()) {
				LOGGER.error('Required template folder $tpl is missing.')
				throw new GradleScriptException("Required template folder $tpl is missing.", null)
			}
		}


	}



	def checkExisting(folderName, folder) {
		def present = folder.exists()
		def res = present ? 'OK' : 'NOK'
		LOGGER.info("Checking $folderName [$folder]\t[$res]" )
		present
	}
}


