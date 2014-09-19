package com.metrixware.gradle.pandoc.project;


import java.io.File;

import com.metrixware.gradle.pandoc.TemplateExtension;

public interface ITemplateProcessor {

	boolean configure(TemplateExtension template,String output, File templateFolder);
	
	
}
