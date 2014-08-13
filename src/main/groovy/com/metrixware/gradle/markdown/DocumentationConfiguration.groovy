/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.markdown

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
