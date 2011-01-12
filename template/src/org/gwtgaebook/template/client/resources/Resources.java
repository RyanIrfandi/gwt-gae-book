package org.gwtgaebook.template.client.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface Resources extends ClientBundle {

	@Source("main.css")
	Style style();

	@Source("logo.png")
	public ImageResource logo();

	@Source("menuBarDownIcon.gif")
	public ImageResource menuBarDownIcon();

	public interface Style extends CssResource {
		// autogenerate, see
		// http://code.google.com/p/google-web-toolkit/wiki/CssResource#Automatically_generating_interfaces

		String anchor();

		String big();

		String block();

		String blue();

		String button();

		String container();

		String formButton();

		String green();

		String label();

		String listing();

		String medium();

		String orange();

		String required();

		String small();
	}

}
