
package com.metrixware.gradle.pandoc



class Repository {
	String url;
	
	Repository(String url){
		this.url = url
	}

	String getUrl() {
		return url
	}

	
	@Override
	String toString() {
		return url
	}
}
