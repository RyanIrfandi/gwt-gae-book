package org.gwtgaebook.template.server.model;

import java.io.Serializable;

import com.google.code.twig.annotation.*;
import com.google.gwt.view.client.ProvidesKey;

//TODO use private fields instead of public in models

@SuppressWarnings("serial")
public class Theater implements Serializable {
	public static final ProvidesKey<Theater> KEY_PROVIDER = new ProvidesKey<Theater>() {
		public Object getKey(Theater t) {
			return (null == t) ? null : t.theaterKey;
		}
	};

	@Store(false)
	public String theaterKey;

	@Index
	public String name;
	public String websiteURL;
	public String photoURL;
	public String phone;

	// TODO set this in Manage theaters
	public String timeZone = "GMT+2:00";

	@Index
	public String locality;
	@Index
	public String region;
	@Index
	public String countryName;
	public String language;
}
