package org.gwtgaebook.CultureShows.shared.model;

import java.util.*;

import com.google.code.twig.annotation.*;

public class Theater {
	String name;
	String URL;
	// String email;
	String language;

	// TODO what about @Child Member[] members ?
	@Embedded
	Set<Member> members; // store as fields rather than entities

	Set<Show> shows;

	Set<Location> locations;

	Schedule schedule;
}
