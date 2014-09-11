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

import com.metrixware.gradle.pandoc.PandocPlugin;

import spock.lang.Specification

class MarkdownPluginSpec extends Specification {
	Project project

	def setup() {
		project = ProjectBuilder.builder().build()
	}

	def "Applies plugin and checks default setup"() {
		expect:
		project.tasks.findByName('checkStructure') == null
		project.tasks.findByName('initDocProject') == null
		project.tasks.findByName('checkTools') == null

		when:
		project.apply plugin: PandocPlugin

		then:
		Task checkStruct = project.tasks.findByName('checkStructure')
		checkStruct != null
		checkStruct.group == 'Documentation'
		
		Task initStruct = project.tasks.findByName('initDocProject')
		initStruct != null
		initStruct.group == 'Documentation'
		
		Task checkTools = project.tasks.findByName('checkTools')
		checkTools != null
		checkTools.group == 'Documentation'
		

	}
}
