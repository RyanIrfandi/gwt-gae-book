package org.gwtgaebook.CultureShows.client.resources;

import com.google.gwt.resources.client.*;

public interface Resources extends ClientBundle {

	@Source("Theatre_Masks.png")
	ImageResource logo();

	@Source("main.css")
	Style style();

	public interface Style extends CssResource {
		// autogenerate, see
		// http://code.google.com/p/google-web-toolkit/wiki/CssResource#Automatically_generating_interfaces
		String big();

		String blue();

		String button();

		String block();

		String green();

		String medium();

		String orange();

		String small();

		String anchor();

		String fieldLabel();

	}

}
