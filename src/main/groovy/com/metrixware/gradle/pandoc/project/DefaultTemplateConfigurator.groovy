package com.metrixware.gradle.pandoc.project

import org.apache.commons.io.FileUtils

import com.metrixware.gradle.pandoc.Template

class DefaultTemplateConfigurator implements ITemplateProcessor {


	@Override
	boolean configure(Template template, String output,File templateFolder) {
		FileUtils.getFile(templateFolder, 'images').mkdir()
		return true
	}
}
