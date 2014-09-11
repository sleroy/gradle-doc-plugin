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

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder

import spock.lang.Specification

import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskInstantiationException;

import com.metrixware.gradle.pandoc.PandocPlugin;

class MarkdownStructureCheckingTaskSpec extends Specification {
	

	def "Checks missing configuration of project"() {
		def Project project = ProjectBuilder.builder().build()
		
		expect:
		project.tasks.findByName('checkStructure') == null

		when:
		project.apply plugin: PandocPlugin
		Task markdownToHtmlTask = project.tasks.findByName('checkStructure')
		markdownToHtmlTask.runTask();

		then:
		thrown(GradleScriptException);				

	}
	
	def "Creates a project; initializes it, check it"() {
		def Project project = ProjectBuilder.builder().build()		
		expect:
		project.tasks.findByName('checkStructure') == null

		when:
		project.apply plugin: PandocPlugin
		project.tasks.findByName('initDocProject').runTask();
		
		Task markdownToHtmlTask = project.tasks.findByName('checkStructure')
		markdownToHtmlTask.runTask();

		then:
		markdownToHtmlTask != null
	
	}
}
