package org.gwtgaebook.CultureShows.client.shows.model;

import java.util.List;

import name.pehl.piriti.client.json.Json;

import com.google.gwt.core.client.GWT;

public class Shows {
	public static final ShowsReader JSON = GWT.create(ShowsReader.class);

	@Json
	public List<Show> shows;
}
