package org.gwtgaebook.CultureShows.shared.model;

public class Member {
	public enum Role {
		ADMINISTRATOR, ARTIST, ASSISTANT
	};

	public String userId;
	public String name;
	public String email;

	public Member.Role role;
}
