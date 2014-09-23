package com.metrixware.gradle.pandoc.project

import org.apache.commons.io.FileUtils

import com.metrixware.gradle.pandoc.TemplateExtension

class DefaultTemplateConfigurator implements ITemplateProcessor {


	@Override
	boolean configure(TemplateExtension template, String output,File templateFolder) {
		FileUtils.getFile(templateFolder, 'images').mkdir()
		return true
	}
}
