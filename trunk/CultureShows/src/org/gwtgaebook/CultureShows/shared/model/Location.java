package org.gwtgaebook.CultureShows.shared.model;

import com.google.code.twig.annotation.Index;

public class Location {
	public String name;
	public String phone;
	public String websiteURL;
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
}
