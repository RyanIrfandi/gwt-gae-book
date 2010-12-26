package org.gwtgaebook.CultureShows.client.shows.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.ProvidesKey;

import name.pehl.piriti.client.json.Json;

public class Show {
	public static final ShowReader JSON = GWT.create(ShowReader.class);

	public static final ProvidesKey<Show> KEY_PROVIDER = new ProvidesKey<Show>() {
		public Object getKey(Show l) {
			return (null == l) ? null : l.showKey;
		}
	};

	@Json
	public String showKey;

	@Json
	public String name;

	@Json
	public String websiteURL;

}
