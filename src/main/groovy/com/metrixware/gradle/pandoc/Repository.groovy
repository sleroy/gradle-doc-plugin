
package com.metrixware.gradle.pandoc



class Repository {
	String name
	String url
	
	Repository(String name){
		this.name = name
	}
	
	void setUrl(String url) {
		this.url = url
	}
	
	String getName() {
		return name
	}

	String getUrl() {
		return url
	}

	
	@Override
	String toString() {
		return url
	}
}
