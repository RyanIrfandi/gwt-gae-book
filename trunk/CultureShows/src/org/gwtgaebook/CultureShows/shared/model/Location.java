package org.gwtgaebook.CultureShows.shared.model;

import com.google.code.twig.annotation.Index;

public class Location {
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	public String websiteURL;
	public String phone;
	public String contactInfo;

	// http://microformats.org/wiki/adr
	public String streetAddress;
	public String extendedAddress;
	@Index
	public String locality;
	public String region;
	public String postalCode;
	@Index
	public String countryName;

	public void setName(String name) {
		this.name = name;
		nameQuery = name.trim().toLowerCase();
	}

	public String getName() {
		return name;

	}

}
