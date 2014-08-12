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

History
-------

### 0.1.0

 * First release

[Daring Fireball]: http://daringfireball.net/projects/markdown/basics
[Gradle Plugin Portal]: http://plugins.gradle.org/
