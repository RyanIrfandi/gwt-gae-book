package org.gwtgaebook.CultureShows.shared.model;

import com.google.appengine.api.datastore.*;
import com.google.code.twig.annotation.Index;

public class TheaterMemberJoin {
	public enum Role {
		ADMINISTRATOR, ARTIST, ASSISTANT
	};

	@Index
	public Key theaterKey;
	@Index
	public Key memberKey;
	@Index
	public Role role;

	// denormalized
	public String theaterName;

	public String memberName;
	public String memberEmail;
}
