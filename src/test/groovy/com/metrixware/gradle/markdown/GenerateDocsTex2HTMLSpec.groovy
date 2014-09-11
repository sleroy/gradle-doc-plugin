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

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import com.metrixware.gradle.pandoc.PandocPlugin;

import spock.lang.Specification

class GenerateDocsTex2HTMLSpec extends Specification {



	def "Generates HTML Documentation from TeX"() {
		def Project project = ProjectBuilder.builder().build()

		when:
		project.apply plugin: PandocPlugin
		project.tasks.findByName('initDocProject').runTask()
		project.tasks.findByName('checkStructure').runTask()
//		project.tasks.findByName('checkTools').runTask()
		
		project.file('docs/articles/').mkdirs();
		File fromFolder = new File('src/test/resources/fakeDoc/');
		FileUtils.copyDirectory(fromFolder, project.rootDir)
		
	
		project.tasks.findByName('copyResources').runTask()
	
		Task markdownToHtmlTask = project.tasks.findByName('generateDocsTex2HTML')
		markdownToHtmlTask.runTask()

		then:
		markdownToHtmlTask != null
		fromFolder.exists();
	
	}
}
