package com.metrixware.gradle.pandoc.eclipse

import com.metrixware.gradle.pandoc.eclipse.Section

class EclipseTocGenerator {
	var String file;
	var String title;

	new(String htmlFile, String title) {
		this.file = htmlFile
		this.title = title
	}
	
	def toc(Section toc) '''
<?xml version="1.0" encoding="UTF-8"?>
<?NLS TYPE="org.eclipse.help.toc"?>

<toc label="«title»" topic="«file»">
«FOR section : toc.subsections»
    «section.generate»
«ENDFOR»
</toc>	
	'''

	def private CharSequence generate(Section section) '''
<topic href="«file»#«section.name»" label="«section.label»">
	«FOR subsection : section.subsections»
	«subsection.generate»
	«ENDFOR»
</topic>'''

}
