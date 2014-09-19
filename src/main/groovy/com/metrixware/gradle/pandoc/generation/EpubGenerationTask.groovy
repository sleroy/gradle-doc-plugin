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
package com.metrixware.gradle.pandoc.generation

import com.metrixware.gradle.pandoc.DocumentExtension
import com.metrixware.gradle.pandoc.TemplateExtension

/**
 * Generates the documentation in EPUB format.
 *
 */
class EpubGenerationTask extends AbstractPandocGenerationTask {



	@Override
	protected boolean isSupported(DocumentExtension doc, String output) {
		return 'epub'.equals(output) && ('tex'.equals(doc.type)||'md'.equals(doc.type))
	}
}


