package org.gwtgaebook.CultureShows.shared.model;

import java.io.Serializable;

import com.google.code.twig.annotation.*;
import com.google.gwt.view.client.ProvidesKey;

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
	@Index
	public String locality;
	@Index
	public String region;
	@Index
	public String countryName;
	public String language;
}
