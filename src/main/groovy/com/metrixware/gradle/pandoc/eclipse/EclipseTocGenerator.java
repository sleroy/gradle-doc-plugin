package com.metrixware.gradle.pandoc.eclipse;

import com.metrixware.gradle.pandoc.eclipse.Section;
import java.util.List;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class EclipseTocGenerator {
  private String file;
  
  private String title;
  
  public EclipseTocGenerator(final String htmlFile, final String title) {
    this.file = htmlFile;
    this.title = title;
  }
  
  public CharSequence toc(final Section toc) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    _builder.newLine();
    _builder.append("<?NLS TYPE=\"org.eclipse.help.toc\"?>");
    _builder.newLine();
    _builder.newLine();
    _builder.append("<toc label=\"");
    _builder.append(this.title, "");
    _builder.append("\" topic=\"");
    _builder.append(this.file, "");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    {
      List<Section> _subsections = toc.getSubsections();
      for(final Section section : _subsections) {
        CharSequence _generate = this.generate(section);
        _builder.append(_generate, "");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</toc>\t");
    _builder.newLine();
    return _builder;
  }
  
  private CharSequence generate(final Section section) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("<topic href=\"");
    _builder.append(this.file, "");
    _builder.append("#");
    String _name = section.getName();
    _builder.append(_name, "");
    _builder.append("\" label=\"");
    String _label = section.getLabel();
    _builder.append(_label, "");
    _builder.append("\">");
    _builder.newLineIfNotEmpty();
    {
      List<Section> _subsections = section.getSubsections();
      for(final Section subsection : _subsections) {
        _builder.append("\t");
        CharSequence _generate = this.generate(subsection);
        _builder.append(_generate, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("</topic>");
    return _builder;
  }
}
