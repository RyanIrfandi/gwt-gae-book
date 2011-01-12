package org.gwtgaebook.template.client.locations.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.ProvidesKey;

import name.pehl.piriti.client.json.Json;

public class Location {
	public transient static final LocationReader jsonr = GWT
			.create(LocationReader.class);
	public transient static final LocationWriter jsonw = GWT
			.create(LocationWriter.class);

	public transient static final ProvidesKey<Location> KEY_PROVIDER = new ProvidesKey<Location>() {
		public Object getKey(Location l) {
			return (null == l) ? null : l.locationKey;
		}
	};

	@Json
	public String locationKey;

	@Json
	public String name;

	@Json
	public String websiteURL;
	// @Json
	public String phone;
	// @Json
	public String contactInfo;

	// @Json
	public String streetAddress;
	// @Json
	public String extendedAddress;
	// @Json
	public String locality;
	// @Json
	public String region;
	// @Json
	public String postalCode;
	// @Json
	public String countryName;

}
