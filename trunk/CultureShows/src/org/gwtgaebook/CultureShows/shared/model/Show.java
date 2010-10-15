package org.gwtgaebook.CultureShows.shared.model;

import com.google.code.twig.annotation.*;

public class Show {
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	public String websiteURL;
	public int minuteDuration;
	public String posterURL;

	public void setName(String name) {
		this.name = name;
		nameQuery = name.trim().toLowerCase();
	}

	public String getName() {
		return name;

	}

}
