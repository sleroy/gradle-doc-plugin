Pandoc/Markdown Gradle Plugin
-------------------------

[![Travis Build Status](http://img.shields.io/travis/sleroy/gradle-pandoc-plugin.svg)](https://travis-ci.org/sleroy/gradle-pandoc-plugin)
[![Coverage Status](http://img.shields.io/coveralls/sleroy/gradle-pandoc-plugin.svg)](https://coveralls.io/r/sleroy/gradle-pandoc-plugin)
[![Semantic Versioning](http://img.shields.io/:semver-0.1.1-blue.svg)](http://semver.org)
[![Bintray](http://img.shields.io/badge/download-latest-bb00bb.svg)](https://bintray.com/sleroy/kordamp/gradle-pandoc-plugin)

This plugin provides a facility for converting markdown into HTML, PDF, EBook. It is based on the [thymeleaf project][]
script by Emanuel Rabina.

See [Daring Fireball][] for syntax basics.


Installation
------------

Use the following snippet

	buildscript { 
    	repositories {
		mavenLocal()
        	jcenter()
        	maven { url 'http://dl.bintray.com/sleroy/maven' }
    	}
    	dependencies {
        	classpath 'com.metrixware:gradle-pandoc-plugin:0.1.0'
    	}
}
apply plugin: 'markdown'


Usage
-----

To create the default project structure to store your documentation, please write the following command :

	gradle initDocProject

By default the documentation is generated into the folder **site** inside your buildDir.

The plugin requires several programs installed on your machine. Basically, on Ubuntu system, install the following packages `pandoc, wkhtmltopdf, calibre, calibre-bin`.

To check if your environment is correctly configured, execute the following command : 

	gradle checkTools
	 
To check if your project structure matches your project documentation configuration : 
	
	gradle checkStructure
	
The default structure looks like : 
	
	├── build
	├── build.gradle
	├── docs
	│   ├── articles
	│   	├── scripts
	│   	├── images	
	│   └── manual
	├── scripts
	├── styles
	├── templates
	└── tmp

*	**docs** : where you will store your markdown files, subfolders are created depending of your document templates (default is `articles`, `manual`)
*	**scripts** : your JS resources
* 	**styles** : your CSS resources
* 	**templates** : your document templates (HTML, EPub)
* 	**tmp** : the temporary ressources and artifacts produced during the configuration will be stored there.	

Configuration
------------------

This class defines the configuration offered by the plugin :


    class DocumentationConfiguration {
        // Folder location
        def folder_docs ='docs'
        def folder_scripts = 'scripts'
        def folder_styles = 'styles'
        def folder_templates = 'templates'
        def folder_output = 'site'
        def folder_outputdoc = 'site/doc'
        def folder_tmp = 'tmp'
        // Templates required to generate the doc and conversion format supported
        def conversions = [
            articles:  ['html', 'pdf'],
            manual: ['html', 'ebook', 'pdf']]
    
        // Binaries 
        def panDocBin = 'pandoc'
        def ebookConvertBin = 'ebook-convert'
        def wkhtmltopdfBin = 'wkhtmltopdf'
        
        // Variables and template variables.
        def buildVariables = [:]
        def templateVariables = [:]
    
        // PDF Options
        def footerFont = 'Open Sans'
        def headerFont = 'Open Sans'
        def pdfDpi = 120
        def marginBottom = 15
        def footerSpacing = 5
        def footerFontSize = 8
        def footerRightText = 'Page [page] of [topage]'
    }
    
	
Templates
---------------------
For more informations about Pandoc templates, go to this [page](http://johnmacfarlane.net/pandoc/demo/example9/templates.html "Pandoc templates")

History
-------

### 0.1.1 Operational version
### 0.1.0 Buggy version

 * First release

[Daring Fireball]: http://daringfireball.net/projects/markdown/basics
[Gradle Plugin Portal]: http://plugins.gradle.org/
[thymeleaf project]: http://www.thymeleaf.org