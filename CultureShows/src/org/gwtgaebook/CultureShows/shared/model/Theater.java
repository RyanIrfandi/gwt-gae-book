package org.gwtgaebook.CultureShows.shared.model;

import java.util.*;

import com.google.code.twig.annotation.*;

public class Theater {
	public String name;
	public String URL;
	// String email;
	public String language;

	// TODO what about @Child Member[] members ?
	@Embedded
	public Set<Member> members; // store as fields rather than entities

	public Set<Show> shows;

	public Set<Location> locations;

	public Schedule schedule;
}
