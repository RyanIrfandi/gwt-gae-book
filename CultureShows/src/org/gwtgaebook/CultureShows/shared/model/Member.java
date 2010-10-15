package org.gwtgaebook.CultureShows.shared.model;

import java.util.*;

import com.google.code.twig.annotation.*;

public class Member {
	@Index
	public String userId;
	@Index
	public String name;
	@Index
	public String email;
	public String websiteURL;
	public String photoURL;
	@Index
	public Date birthDate;
}
