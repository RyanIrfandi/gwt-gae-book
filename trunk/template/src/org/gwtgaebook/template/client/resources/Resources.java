package org.gwtgaebook.template.client.resources;

import com.google.gwt.resources.client.*;

public interface Resources extends ClientBundle {

	// @Source("logo.png")
	// ImageResource logo();

	@Source("main.css")
	Style style();

	public interface Style extends CssResource {
		// autogenerate, see
		// http://code.google.com/p/google-web-toolkit/wiki/CssResource#Automatically_generating_interfaces
	}

}
