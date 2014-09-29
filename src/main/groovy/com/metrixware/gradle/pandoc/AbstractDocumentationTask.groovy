package com.metrixware.gradle.pandoc

import java.text.SimpleDateFormat

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.text.StrSubstitutor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class AbstractDocumentationTask extends DefaultTask {

	static final String DOCS = 'docs'
	static final String TEMPLATES = 'templates'
	static final String SOURCES = 'sources'
	static final String TMP = 'tmp'


	File rootFolder
	File templatesFolder
	File sourcesFolder
	File outputDir
	File tmpFolder
	File tmpTemplatesFolder
	File tmpSourcesFolder



	@TaskAction
	void runTask(){
		initialize()
		process()
	}

	protected abstract void process()

	private void initialize(){
		def docRootDirectory = project.documentation.docsDirectory
		if(docRootDirectory.startsWith('/')){
			rootFolder  = new File(docRootDirectory)
		}else{
			rootFolder = project.file(docRootDirectory)
		}
		templatesFolder = FileUtils.getFile(rootFolder,TEMPLATES)
		sourcesFolder = FileUtils.getFile(rootFolder,SOURCES)

		String docOutputDirectory = project.documentation.outputDirectory
		if(docOutputDirectory.startsWith('/')){
			outputDir  = new File(docOutputDirectory)
		}else{

			outputDir = new File(project.projectDir,docOutputDirectory)
		}
		String docTempDirectory = project.documentation.tmpDirectory
		if(docTempDirectory.startsWith('/')){
			tmpFolder  = new File(docTempDirectory)
		}else{
			tmpFolder = new File(project.projectDir,docTempDirectory)
		}
		tmpTemplatesFolder =FileUtils.getFile(tmpFolder,TEMPLATES)
		tmpSourcesFolder =FileUtils.getFile(tmpFolder,SOURCES)
	}

	Collection<Template> getTemplates(){
		return project.documentation.templates
	}

	Collection<Document> getDocuments(){
		return project.documentation.documents
	}
	
	Collection<Repository> getRepositories(){
		return project.documentation.repositories
	}

	File getTemplateFolder(Template template){
		return FileUtils.getFile(templatesFolder, template.name)
	}

	File getDocumentFolder(Document doc){
		return FileUtils.getFile(sourcesFolder, doc.name)
	}


	File getDocumentFolder(Document doc, String lang){
		return FileUtils.getFile(getDocumentFolder(doc), lang)
	}


	File getDocumentSourceFile(Document doc, String lang){
		return FileUtils.getFile(getDocumentFolder(doc,lang),doc.name+'-'+lang+'.'+doc.type)
	}

	File getDocumentPropertiesFile(Document doc, String lang){
		return FileUtils.getFile(getDocumentFolder(doc,lang),doc.name+'-'+lang+'.properties')
	}

	File getTemplateFolder(Template template, String output){
		return FileUtils.getFile(getTemplateFolder(template), output)
	}

	File getTemplateFile(Template template, String output){
		return FileUtils.getFile(getTemplateFolder(template,output), 'template.tpl')
	}

	File getTempTemplateFile(Document doc,Template template,String lang, String output){
		return FileUtils.getFile(getTempOutputFolder(doc,template,lang,output),'template.tpl')
	}

	File getTempTemplateFolder(Template template, String output){
		return FileUtils.getFile(tmpTemplatesFolder, template.name,output)
	}

	boolean hasTemplate(Template template, String output){
		
		File templateFile = FileUtils.getFile(getTempTemplateFolder(template, output),'template.tpl')
		return templateFile.exists() && !templateFile.text.isEmpty()
	}

	File getTempOutputFolder(Document doc,Template template,String lang, String output){
		return FileUtils.getFile(getTempOutputFolder(doc),template.name,lang,output)
	}

	File getTempOutputFolder(Document doc){
		return FileUtils.getFile(tmpSourcesFolder, doc.name)
	}

	File getTempSourceDocument(Document doc, Template template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${doc.type}")
	}

	File getTempOutputDocument(Document doc, Template template, String lang, String output){
		def dir = getTempOutputFolder(doc, template, lang, output)
		return FileUtils.getFile(dir, "${doc.name}-${lang}.${output}")
	}



	String[] getSupportedDocumentTypes(){
		return ['tex', 'md', 'lyx']
	}

	String[] getSupportedOutputTypes(){
		return ['pdf', 'html']
	}

	protected  void preprocess(File file, Properties variables){
		StrSubstitutor replacer = new StrSubstitutor(variables)
		replacer.setVariablePrefix('@')
		replacer.setVariableSuffix('@')
		String content = FileUtils.readFileToString(file)
		String replaced = replacer.replace(content)
		FileUtils.write(file, replaced)
	}


	Properties getGlobalVariables() {
		def documentVersion = new SimpleDateFormat('yyyyMMdd - dd MMMM yyyy', Locale.ENGLISH).format(new Date())
		def magicVariablesMap = new Properties()
		magicVariablesMap['documentVersion']=  documentVersion.toString()
		magicVariablesMap['date']=  new Date().toString()
		magicVariablesMap['projectVersion']=  project.version.toString()
		magicVariablesMap.putAll(project.documentation.templateVariables)
		return magicVariablesMap
	}

	Properties getDocumentVariables(Document document, String lang) {
		def properties = new Properties()
		properties.load(FileUtils.openInputStream(getDocumentPropertiesFile(document, lang)))
		return properties
	}
}
