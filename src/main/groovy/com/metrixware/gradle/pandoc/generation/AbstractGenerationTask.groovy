/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License')
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
package com.metrixware.gradle.pandoc.generation

import org.apache.commons.io.FileUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.AbstractDocumentationTask
import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Template
/**
 * Abstract documentation generation task. It process all tuples of (document,template,output,language).
 * @author afloch
 *
 */
abstract class AbstractGenerationTask extends AbstractDocumentationTask {
	

	protected void process() {
		for(Document document : documents){
			def supported = templates.findAll {Template t -> document.support(t)}
			for(Template template : supported){
				for(String output : template.outputs){
					if(isSupported(document,output)){
						for(String lang : document.languages){
							def dir = getTempOutputFolder(document, template, lang, output)
							dir.mkdirs()
							println("Generate document ${document.name} ${template.name} [${output},${lang}]")
							process(dir, document,template,lang,output)
						}
					}
				}
			}
		}
	}
	protected abstract boolean isSupported(Document doc, String output)

	protected abstract void process(File folder,Document doc, Template template, String lang, String output)

	File getTempSourceDocument(Document doc, Template template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${doc.type}")
	}

	File getTempOutputDocument(Document doc, Template template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${output}")
	}

	File getDocumentOutputFolder(Document doc, Template template, String lang, String output){
		return FileUtils.getFile(outputDir,doc.name,template.name,lang,output)
	}

	File getDocumentOutputFile(Document doc, Template template, String lang, String output){
		return FileUtils.getFile(getDocumentOutputFolder(doc,template,lang,output),"${doc.name}-${lang}.${output}")
	}
}

