[![Travis Build Status](http://img.shields.io/travis/sleroy/gradle-pandoc-plugin.svg)](https://travis-ci.org/sleroy/gradle-doc-plugin)
[![Coverage Status](http://img.shields.io/coveralls/sleroy/gradle-pandoc-plugin.svg)](https://coveralls.io/r/sleroy/gradle-doc-plugin)
[![Semantic Versioning](http://img.shields.io/:semver-0.1.1-blue.svg)](http://semver.org)
[![Bintray](http://img.shields.io/badge/download-latest-bb00bb.svg)](https://bintray.com/sleroy/kordamp/gradle-doc-plugin)

Overview
==

This plugin provides a facility for converting markdown and LaTeX into HTML, PDF, EPUB and Eclipse documentations output. 

See [Daring Fireball][] for markdown syntax basics.

See [here](http://www.andy-roberts.net/writing/latex) for some LaTeX tutorials.



Prerequisites
==

The plugin requires several programs installed on your machine. Basically, on Ubuntu system, install the following packages `pandoc, latex`.

Obviously, [gradle](http://www.gradle.org/) should be installed and located in your $PATH variable. The plugin has been developped and tested for gradle v2.1.

Quickstart
==


## Installation


Insert the following snippet in your build.gradle of your gradle project.

	buildscript { 
    	repositories {
			mavenLocal()
        	jcenter()
        	maven { url 'http://dl.bintray.com/sleroy/maven' }
    	}
    	dependencies {
        	classpath 'com.metrixware:gradle-doc-plugin:0.1.2+'
    	}
	}
	apply plugin: 'pandoc'

## Configuration

Next step is to configure your documentation project. You will declare a single document and single template for this document. Insert the following lines in your build.gradle to configure the project to generate HTML and PDF documentation from a single markdown file.


	documentation{
		templates{
			template{
				name = 'standard'
				outputs =['html','pdf']
			}		
		}
	
		documents{
			document{
				name = 'example'
				type = 'md'
				languages=['en']
			}
		}
	}

The `output` field of a template specifies the supported outputs of the template (html, pdf, epub or eclipse).
	
To create the default project structure to store your documentation, please write the following command :

	gradle pandoc-configure

The default structure looks like : 
	
	├── build
	├── build.gradle
	├── docs
	│   ├── sources
	│   └── templates

Documentation project structure:
	
*	**docs/sources** : where you will store your source document files, subfolders are created depending on the documents names and languages. 
* 	**docs/templates** : your  templates that may be used by any of the documents of docs/sources.


## Fill your document

Insert the following dummy markdown content into the `docs/source/example/en/example-en.md` file that has been automatically created by the previous configuration step.

	
	My Document
	============

	An h2 header
	------------
	Paragraphs are separated by a blank line.

	2nd paragraph. *Italic*, **bold**, and `monospace`. Itemized lists
	look like:

 	 * this one
 	 * that one
 	 * the other one


	An h2 header
	------------

	Here's a numbered list:
 	 1. first item
 	 2. second item
	 3. third item

	Note again how the actual text starts at 4 columns in (4 characters
	from the left side). Here's a code sample:

    	# Let me re-iterate ...
    	for i in 1 .. 10 { 
    		do-something(i) 
		}

## Documentation generation

To generate all the output of your document simply write command:

	gradle pandoc-all

By default, the documentation is generated into the folder **site** inside your buildDir. The build dir should have the following structure:

	├── build/
	│   ├── site/
	│	│   ├── example
	│	│   │	├── standard/
	│	│	│	│	├── en/
	│	│	│	│	│   ├── html/
	│	│	│	│	│   ├── pdf/
	│   └── tmp/

Thus, to see the PDF document of your documentation, open the file:
 `build/site/example/standard/en/pdf/example-en.pdf`

To match your design needs, you can customize the generated documents through a template mechanism detailled in the user guide.

User guide
===


## Available tasks

To see the list of available tasks, enter following command:

	gradle tasks

Here is a shot description of each documentation task provided by the gradle pandoc plugin.

### Generation tasks
 * pandoc-all - Produce all documentations. It call all other tasks.
 * pandoc-eclipse - Produce all Eclipse documentations.
 * pandoc-epub - Produce all EBooks documentations.
 * pandoc-html - Produce all HTML documentations.
 * pandoc-pdf - Produce all PDF documentations. 
 * pandoc-md2pdf - Produce all PDF documentations from markdown sources.
 * pandoc-tex2pdf - Produce all PDF documentations from LaTeX sources.
 
### Project tasks
  * pandoc-configure - Initializes the project structure directories from the templates definition. This task will be called by any documentation task
 * pandoc-prepare - Copy the documentation resources into the temporary folder to produce the documentation. This task will be called by any documentation generation task.	
	
## Configuration of a documentation project

### Global configuration

You can customize the documentation generation parameters directly in the gradle build file.
Example below show a specific configuration that generate the output document in 'myOutput/' directory of your gradle project.

	documentation{
		outputDirectory='myOutput/'
		...
	}

The next pragraphs shows the different customization options of the global configuration.

#### Binaries paths

|  Variable   |  Default value | 
|:----------|:-------------:|
|panDocBin 		|  pandoc|
|pdfTexBin 		|   xelatex   |  

#### Generation paths

Generation paths may be relative to the gradle project or absolute.

|  Variable   |  Default value |  Explanation | 
|----------:|:-------------:|:-------------:|
|docsDirectory 		|  docs| sources and templates directory |
|tmpDirectory 		|  build/tmp  |  temporary directory that contains all merged sources/templates |
|outputDirectory 	|  build/site  |  directory that will contains the output documents |

#### Injectables files

|  Variable   |  Default value |  Explanation | 
|----------:|:-------------:|:-------------:|
|sources 		|  ['tex','html','sty','md','tpl'] | files extensions that will be injected with the variables |

### Templates

Templates are used to customize the layout of a documentation. It adapt the same document file to many kinds of differents formatting and/or outputs.
For example, the same documentation source file can be associated to two distincts templates that produce both html and PDF outputs.

#### Templates definitions
Templates are defined in the `templates` section of your documentation configuration (in the `build.gradle` file). See following example to declare a template for html and pdf documents generation.

	documentation{
		templates{
			template{
				name = 'standard'
				outputs =['html','pdf']
			}		
		}
		...
	}

A template definition use two parameters:

 * name : name of the template.
 * outputs : outputs of the template (default : html, supported : html, pdf, epub, eclipse) 

#### Template customization

For each template defined in the configuration a directory will be created (by `pandoc-configure` task), by default, in the `docs/` directory of your project. This directory will contains an subdirectory for each supported output.

	├── docs/
	│   ├── templates/
	│	│   ├── standard
	│	│   │	├── html/
	|	│	│   │	├── template.tpl
	│	│   │	├── pdf/	
	|	|	│	│   	├── template.tpl

The `template.tpl` is a text document that you may edit to customize the formatting of the output. The content of this file depends on the output. For example, if you customize the html template, then the content of the  `template.tpl` has to be in HTML format.

If the template file is empty then pandoc will use a default template that may not fit your needs.

Below is an example of HTML template to build an XHTML webpage that manage the encoding.

	<!DOCTYPE html>

	<html>
	<head>
		<meta charset="utf-8">
	</head>
	<body>
		$body$
	</body>
	</html>

The `$body$` variable will be used pandoc to inject your document body into the template. Below is a selection of templates variables used by pandoc:

* body : body of document
* title : title of document
* toc : table of contents
* lof : list of figures
* lot : list of tables

Each of these variables has to be surrounded by `$` symbol in your template.
For advanced informations about how to use pandoc templates, see the [pandoc documentation](http://johnmacfarlane.net/pandoc/README.html#templates).



#### Templates languages mapping

Below tables gives the language mapping for each supported template output. For example, for a PDF output, the template has to be written in tex format.

|  Output   |  Template Language | 
|----------:|:-------------:|
|html 		|  html|
|pdf 		|   tex   |  
|epub 		| tex| 
|eclipse 	| html | 


#### Variables

You can use two types of variables in your templates.

**Pandoc variables** (syntax : $var$). Those variables are used by pandoc to compute the merge of the template  and the source document.

**Document variables** (syntax : @var@). Those variables are specific to the gradle plugin and can be used to specify a logo image path in a template for example. Those variables are defined in a .properties file in the source document directory.

Below is an example of LaTeX template that use both of variable types:

	\documentclass[a4paper,11pt]{book}
	\usepackage{guide}

	\makeindex
	\begin{document}
	
	\frontmatter
	\begin{titlepage}
   	  \begin{figure}[H]
      	 \vspace{-2cm}
      	 \hspace{2cm}
      	 \includegraphics[width=\textwidth]{@logo@}
      	 \vspace{-2cm}
      \end{figure}
	\end{titlepage}

	\mainmatter
		$body$
	\end{document}

### Documents

A document is a source that can be  written in `tex` or `md` formats. It will be used to generate all the supported outputs of your documentation.

A document is mapped to one or many templates defined in your project ()see previous section).
 
#### Documents definitions

Documents are defined in the `documents` section of your documentation configuration (in the `build.gradle` file). See following example to declare a markdown document that use all available templates.

	documentation{
		document{
			name= 'example'
			type= 'md'
			languages =['en','fr']
		}	
		...
	}

A document definition use following parameters:

 * name : name of the document. **[mandatory]**
 * type : source format of the document (supported : md, tex) **[optionnal, default : 'md']**
 * languages : array of languages for the document.  **[optionnal, default: ['en']]**
 * templates : array of the templates used by the document   **[optionnal, default : ['all']]**

#### Write a document

For each document defined in the configuration a directory will be created (by `pandoc-configure` task), by default, in the `docs/sources/` directory of your project. This directory will contains a directory for each supported language.

Below is a example of generated directory structure for a markdown document named 'example'  that will support english and french languages.

	├── docs/
	│   ├── sources/
	│	│   ├── example
	│	│   │	├── en/
	|	│	│   │	├── example-en.md
	|	│	│   │	├── example-en.properties	
	│	│   │	├── fr/	
	|	|	│	│   	├── example-fr.md
	|	│	│   │	├── example-fr.properties

The `.properties` file in each document directory specifies all document variables provided by the document. Those variables may be used in the template or in the document itself.

Below is an example of .properties file:

	title=My example document
	subtitle= version 1.0
	logo=images/frontpage.png

Variables can be used in the source document and template using the notation :`@var@`.


Changelog
===

#### 0.1.2 
 * Add support of LaTeX sources 
 * Add Eclipse doc project output
 * Manage languages
 * Configuration based on document and temlates
 * Change all generated directories structure
 * Rename all tasks to pandoc-*
 
#### 0.1.1
 * Fix some bugs
 
#### 0.1.0 	     
 * First release

[Daring Fireball]: http://daringfireball.net/projects/markdown/basics
[Gradle Plugin Portal]: http://plugins.gradle.org/
[thymeleaf project]: http://www.thymeleaf.org
