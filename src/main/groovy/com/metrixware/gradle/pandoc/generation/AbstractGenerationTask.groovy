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

import com.metrixware.gradle.pandoc.AbstractDocumentationTask
import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension
/**
 * Abstract documentation generation task. It process all tuples of (document,template,output,language).
 * @author afloch
 *
 */
abstract class AbstractGenerationTask extends AbstractDocumentationTask {



	protected void process() {
		for(DocumentExtension document : documents){
			def supported = templates.findAll {TemplateExtension t -> document.support(t)}
			for(TemplateExtension template : supported){
				for(String output : template.outputs){
					if(isSupported(document,output)){
						for(String lang : document.languages){
							def dir = getTempOutputFolder(document, template, lang, output)
							dir.mkdirs()
							println("\nGenerate document ${document.name} ${template.name} [${output},${lang}]")
							process(dir, document,template,lang,output)
						}
					}
				}
			}
		}
	}
	protected abstract boolean isSupported(DocumentExtension doc, String output)

	protected abstract void process(File folder,DocumentExtension doc, TemplateExtension template, String lang, String output)

	File getTempSourceDocument(DocumentExtension doc, TemplateExtension template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${doc.type}")
	}

	File getTempOutputDocument(DocumentExtension doc, TemplateExtension template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${output}")
	}

	File getDocumentOutputFolder(DocumentExtension doc, TemplateExtension template, String lang, String output){
		return FileUtils.getFile(outputDir,doc.name,template.name,lang,output)
	}

	File getDocumentOutputFile(DocumentExtension doc, TemplateExtension template, String lang, String output){
		return FileUtils.getFile(getDocumentOutputFolder(doc,template,lang,output),"${doc.name}-${lang}.${output}")
	}
}

