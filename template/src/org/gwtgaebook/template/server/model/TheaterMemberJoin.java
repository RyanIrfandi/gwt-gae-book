package org.gwtgaebook.template.server.model;

import com.google.code.twig.annotation.*;

public class TheaterMemberJoin {
	public enum Role {
		ADMINISTRATOR, ARTIST, ASSISTANT
	};

	@Index
	public String theaterKey;
	@Index
	public String memberKey;
	@Index
	public Role role;

	// denormalized
	public String theaterName;

	public String memberName;
	public String memberEmail;
}
