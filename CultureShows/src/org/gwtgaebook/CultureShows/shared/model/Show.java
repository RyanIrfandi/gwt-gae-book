package org.gwtgaebook.CultureShows.shared.model;

import com.google.code.twig.annotation.*;
import com.google.gson.annotations.Expose;

public class Show {
	@Store(false)
	@Expose
	public String showKey;

	@Expose
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	@Expose
	public String websiteURL;

	@Expose
	public int minuteDuration;

	@Expose
	public String posterURL;

	public void setName(String name) {
		this.name = name;
		nameQuery = name.trim().toLowerCase();
	}

	public String getName() {
		return name;

	}

}