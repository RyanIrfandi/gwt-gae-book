package org.gwtgaebook.CultureShows.shared.model;

import com.google.code.twig.annotation.*;

public class Theater {
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
