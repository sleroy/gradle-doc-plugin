package com.metrixware.gradle.pandoc.project

import java.io.File

import org.apache.commons.io.FileUtils

import com.metrixware.gradle.pandoc.Template

 class HtmlTemplateConfigurator implements ITemplateProcessor {

	@Override
	 boolean configure(Template template,String output, File folder) {
		if (output.equals('html')) {
			FileUtils.getFile(folder, 'images').mkdir()
			FileUtils.getFile(folder, 'css').mkdir()
			FileUtils.getFile(folder, 'scripts').mkdir()
			return true
		}
		return false
	}

}
