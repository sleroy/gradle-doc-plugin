package com.metrixware.gradle.pandoc.generation

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension
import com.metrixware.gradle.pandoc.postprocess.Latex2HtmlReferencesPostprocessor

class HtmlGenerationTask extends AbstractPandocGenerationTask{

	@Override
	protected boolean isSupported(DocumentExtension doc, String output) {
		return 'html'.equals(output) && ('tex'.equals(doc.type)||'md'.equals(doc.type))
	}

	@Override
	protected String getPandocOutput(String output) {
		return 'html5'
	}

	@Override
	protected int getTocDepth() {
		return 2
	}

	@Override
	protected void copyToSite(File folder, DocumentExtension doc,
			TemplateExtension template, String lang, String output) {
		def out =  getDocumentOutputFile(doc, template, lang, output)
		project.copy {
			from(folder) {
				include 'images/**'
				include 'scripts/**'
				include 'css/**'
			}
			into getDocumentOutputFolder(doc, template, lang, output)
		}
		FileUtils.copyFile(getTempOutputDocument(doc, template, lang, output),out)
		def postprocess = new Latex2HtmlReferencesPostprocessor('utf-8')
		postprocess.process(out)
	}
}
