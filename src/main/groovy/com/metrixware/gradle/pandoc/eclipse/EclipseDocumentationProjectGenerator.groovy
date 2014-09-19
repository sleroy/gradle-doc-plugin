package com.metrixware.gradle.pandoc.eclipse;

import com.metrixware.gradle.pandoc.eclipse.EclipseTocGenerator;
import com.metrixware.gradle.pandoc.eclipse.Section;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Exceptions;

@SuppressWarnings("all")
public class EclipseDocumentationProjectGenerator {
  public String project = "org.unknown.plugin.doc";
  
  public String name = "Unknown Plugin";
  
  public String vendor = "";
  
  public String version = "1.0.O.qualifier";
  
  public String lang = "en";
  
  public void generate(final File directory, final Section toc, final String htmlFile) {
    try {
      final File projectDir = this.getProjectDirectory(directory);
      final File mf = FileUtils.getFile(projectDir, "META-INF", "MANIFEST.MF");
      File _parentFile = mf.getParentFile();
      _parentFile.mkdir();
      CharSequence _manifest = this.manifest();
      FileUtils.write(mf, _manifest);
      File _file = FileUtils.getFile(projectDir, ".project");
      CharSequence _projectFile = this.projectFile();
      FileUtils.write(_file, _projectFile);
      File _file_1 = FileUtils.getFile(projectDir, "plugin.xml");
      CharSequence _plugin = this.plugin();
      FileUtils.write(_file_1, _plugin);
      File _file_2 = FileUtils.getFile(projectDir, "build.properties");
      CharSequence _buildProperties = this.buildProperties();
      FileUtils.write(_file_2, _buildProperties);
      final EclipseTocGenerator tocgenerator = new EclipseTocGenerator(htmlFile, this.name);
      File _file_3 = FileUtils.getFile(projectDir, "toc.xml");
      CharSequence _c = tocgenerator.toc(toc);
      FileUtils.write(_file_3, _c);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public File getProjectDirectory(final File directory) {
    final File projectDir = FileUtils.getFile(directory, this.project);
    projectDir.mkdirs();
    return projectDir;
  }
  
  private CharSequence plugin() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<?eclipse version=\"3.4\"?>");
    _builder.newLine();
    _builder.append("<plugin>");
    _builder.newLine();
    _builder.append("   ");
    _builder.append("<extension");
    _builder.newLine();
    _builder.append("         ");
    _builder.append("point=\"org.eclipse.help.toc\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("<toc");
    _builder.newLine();
    _builder.append("            ");
    _builder.append("file=\"toc.xml\"   primary=\"true\">");
    _builder.newLine();
    _builder.append("      ");
    _builder.append("</toc>");
    _builder.newLine();
    _builder.append("   ");
    _builder.append("</extension>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("</plugin>\t");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence manifest() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("Manifest-Version: 1.0");
    _builder.newLine();
    _builder.append("Bundle-ManifestVersion: 2");
    _builder.newLine();
    _builder.append("Bundle-Name: ");
    _builder.append(this.name, "");
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-SymbolicName: ");
    _builder.append(this.project, "");
    _builder.append(";singleton:=true");
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-Version: ");
    _builder.append(this.version, "");
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-Vendor: ");
    _builder.append(this.vendor, "");
    _builder.newLineIfNotEmpty();
    _builder.append("Bundle-RequiredExecutionEnvironment: JavaSE-1.6");
    _builder.newLine();
    _builder.append("Require-Bundle: org.eclipse.help;bundle-version=\"3.5.100\"");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence projectFile() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<projectDescription>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<name>");
    _builder.append(this.name, "\t");
    _builder.append("</name>");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("<comment></comment>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<projects>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</projects>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<buildSpec>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<name>org.eclipse.pde.ManifestBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<buildCommand>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<name>org.eclipse.pde.SchemaBuilder</name>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("<arguments>");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("</arguments>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("</buildCommand>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</buildSpec>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("<natures>");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("<nature>org.eclipse.pde.PluginNature</nature>");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("</natures>");
    _builder.newLine();
    _builder.append("</projectDescription>");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence buildProperties() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("bin.includes = META-INF/,\\");
    _builder.newLine();
    _builder.append("               ");
    _builder.append(".,\\");
    _builder.newLine();
    _builder.append("               ");
    _builder.append("plugin.xml,\\");
    _builder.newLine();
    _builder.append("               ");
    _builder.append("html/,\\");
    _builder.newLine();
    _builder.append("               ");
    _builder.append("toc.xml");
    _builder.newLine();
    return _builder;
  }
}
