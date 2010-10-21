package org.gwtgaebook.CultureShows.client;

import java.util.HashMap;
import java.util.Map;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

public class UserInfoStatic {
	public static UserInfo userInfo;

	// theaters user has access to; key/name
	public static Map<String, String> theatersMap = new HashMap<String, String>();

	public static String currentTheaterKey;
}
