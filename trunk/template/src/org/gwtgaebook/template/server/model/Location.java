package org.gwtgaebook.template.server.model;

import java.io.Serializable;

import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Store;
import com.google.gson.annotations.Expose;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Location implements Serializable {
	@Store(false)
	@Expose
	public String locationKey;

	@Expose
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	@Expose
	public String websiteURL;
	@Expose
	public String phone;
	@Expose
	public String contactInfo;

	// http://microformats.org/wiki/adr
	@Expose
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
