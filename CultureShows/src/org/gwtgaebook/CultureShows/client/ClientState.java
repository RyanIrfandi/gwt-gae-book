package org.gwtgaebook.CultureShows.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

public class ClientState {
	public UserInfo userInfo;

	// theaters user has access to; key/name
	public List<Theater> theaters = new ArrayList<Theater>();

	public String currentTheaterKey;
}
