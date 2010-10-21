package org.gwtgaebook.CultureShows.client;

import java.util.HashMap;
import java.util.Map;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

public class ClientState {
	public UserInfo userInfo;

	// theaters user has access to; key/name
	public Map<String, String> theatersMap = new HashMap<String, String>();

	public String currentTheaterKey;
}
