/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
package com.metrixware.gradle.pandoc.project



import net.lingala.zip4j.core.ZipFile
import net.lingala.zip4j.model.ZipParameters

import org.apache.commons.io.FileUtils

import com.metrixware.gradle.pandoc.AbstractDocumentationTask

/**
 * Build a templates repository from the templates of the documentation project
 * @author afloch
 *
 */
class BuildRepositoryTask extends AbstractDocumentationTask {


	protected void process() {

		def repDirectory = FileUtils.getFile(project.getBuildDir(),"repository")
		repDirectory.deleteDir()
		repDirectory.mkdirs()
		def out = FileUtils.getFile(repDirectory,"templates.zip")
		println("Build repository ${out} for templates ${templates}")
		ZipFile zip = new ZipFile(out)
		for(File template : getTemplatesFolder().listFiles()){
			if(template.isDirectory())
				zip.addFolder(template, new ZipParameters())
		}
	}
}


