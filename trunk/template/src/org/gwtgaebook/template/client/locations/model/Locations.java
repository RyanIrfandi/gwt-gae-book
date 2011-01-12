package org.gwtgaebook.template.client.locations.model;

import java.util.List;

import name.pehl.piriti.client.json.Json;

import com.google.gwt.core.client.GWT;

public class Locations {
	public static final LocationsReader JSON = GWT
			.create(LocationsReader.class);

	@Json
	public List<Location> locations;
	// = new ArrayList<Location>()
}
