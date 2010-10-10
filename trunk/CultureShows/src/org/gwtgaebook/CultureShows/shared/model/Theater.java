package org.gwtgaebook.CultureShows.shared.model;

import java.util.*;

import com.google.code.twig.annotation.*;

public class Theater {
	public String name;
	public String URL;
	public String language;

	public List<Member> members = new ArrayList<Member>();

	public List<Show> shows = new ArrayList<Show>();

	public List<Location> locations = new ArrayList<Location>();

	public List<Performance> performances = new ArrayList<Performance>();
}
