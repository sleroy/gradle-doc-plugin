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

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils;
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.PandocPlugin
import com.metrixware.gradle.pandoc.Template

class BuildRepositorySpec extends Specification {



	def "Build templates repository"() {
		
			//prepare project
		def Project project = ProjectBuilder.builder().build()
		
		File fromFolder = new File('src/test/resources/fakeDoc/')
		FileUtils.copyDirectory(fromFolder, project.rootDir)
		def document = new Document("example")
		

		
		when:
		project.apply plugin: PandocPlugin
		project.documentation.documents.add(document)
		
		
		project.tasks.findByName('pandoc-configure').runTask()
		project.tasks.findByName('pandoc-prepare').runTask()
		project.tasks.findByName('pandoc-repository').runTask()

	
		then:
		def repo = FileUtils.getFile(project.getBuildDir(),"repository")
		repo.exists()
		
		expect:
			repo.listFiles().length ==1
	}
}
