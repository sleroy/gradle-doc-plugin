package com.metrixware.gradle.markdown.postprocess;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public abstract class AbstractStringPostprocessor implements IPostProcessor {
	protected static Logger LOGGER = Logger
			.getLogger(AbstractStringPostprocessor.class);
	private String encoding;

	public AbstractStringPostprocessor(String encoding) {
		super();
		this.encoding = encoding;

	}

	@Override
	public void process(InputStream input, OutputStream output) {

		try {
			String inString = readInputString(input);

			String result = process(inString);
			IOUtils.write(result, output);

		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}

	}

	public void process(File input) throws IOException {
		InputStream instream = new BufferedInputStream( FileUtils.openInputStream(input));
		File tmp = File
				.createTempFile(input.getCanonicalPath(), ".tmp");
		OutputStream outstream = new BufferedOutputStream(FileUtils.openOutputStream(tmp));
		process(instream, outstream);
		instream.close();
		outstream.close();
		FileUtils.copyFile(tmp, input);
	}

	protected abstract String process(String inString);

	private String readInputString(InputStream input) throws IOException {
		StringWriter writer = new StringWriter();

		IOUtils.copy(input, writer, encoding);
		return writer.toString();
	}
}
