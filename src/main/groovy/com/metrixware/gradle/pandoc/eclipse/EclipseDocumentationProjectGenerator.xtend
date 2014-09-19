package com.metrixware.gradle.pandoc.eclipse

import java.io.File

import static extension org.apache.commons.io.FileUtils.*

class EclipseDocumentationProjectGenerator {
	public String project="org.unknown.plugin.doc";
	public String name="Unknown Plugin";
	public String vendor="";
	public String version= "1.0.O.qualifier";
	public String lang="en";

	

	def void generate(File directory, Section toc, String htmlFile){
		val projectDir = getProjectDirectory(directory)

		val mf  = projectDir.getFile("META-INF","MANIFEST.MF")
		mf.parentFile.mkdir
		mf.write(manifest)

		projectDir.getFile(".project").write(projectFile)
		projectDir.getFile("plugin.xml").write(plugin)
		projectDir.getFile("build.properties").write(buildProperties)
		
		val tocgenerator = new EclipseTocGenerator(htmlFile, name)
		projectDir.getFile("toc.xml").write(tocgenerator.toc(toc))

	}
	
	def File getProjectDirectory(File directory){
		val projectDir = directory.getFile(project)
		projectDir.mkdirs
		return  projectDir
	}


	private def plugin()'''
<?xml version="1.0" encoding="UTF-8"?>
<?eclipse version="3.4"?>
<plugin>
   <extension
         point="org.eclipse.help.toc">
      <toc
            file="toc.xml"   primary="true">
      </toc>
   </extension>

</plugin>	
	'''
	
	
	private def manifest()'''
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: «name»
Bundle-SymbolicName: «project»;singleton:=true
Bundle-Version: «version»
Bundle-Vendor: «vendor»
Bundle-RequiredExecutionEnvironment: JavaSE-1.6
Require-Bundle: org.eclipse.help;bundle-version="3.5.100"
	'''
	
	private def projectFile()'''
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>«name»</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.pde.ManifestBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>org.eclipse.pde.SchemaBuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.pde.PluginNature</nature>
	</natures>
</projectDescription>
	
'''

	private def buildProperties()'''
bin.includes = META-INF/,\
               .,\
               plugin.xml,\
               html/,\
               toc.xml
'''

}