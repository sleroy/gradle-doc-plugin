package com.metrixware.gradle.pandoc.project;


import java.io.File;

import com.metrixware.gradle.pandoc.Template;

public interface ITemplateProcessor {

	boolean configure(Template template,String output, File templateFolder);
	
	
}
