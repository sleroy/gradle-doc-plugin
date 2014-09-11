package com.metrixware.gradle.markdown.postprocess;

import java.io.InputStream;
import java.io.OutputStream;

public interface IPostProcessor {

	void process(InputStream input, OutputStream output);

}
