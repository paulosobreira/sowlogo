package br.nnpe;

import java.net.URL;

public class SowNode {
	private String title;
	private URL url;

	public SowNode(String title, URL url) {
		super();
		this.title = title;
		this.url = url;
	}


	@Override
	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}
}
