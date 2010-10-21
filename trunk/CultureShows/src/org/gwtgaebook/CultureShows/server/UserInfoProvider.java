package org.gwtgaebook.CultureShows.server;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.inject.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UserInfoProvider implements Provider<UserInfo> {

	public UserInfo get() {
		UserInfo userInfo = new UserInfo();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			userInfo.isSignedIn = true;
			userInfo.email = user.getEmail();
			userInfo.userId = user.getUserId();
		} else {
			userInfo.isSignedIn = false;
		}

		return userInfo;
	}
}
