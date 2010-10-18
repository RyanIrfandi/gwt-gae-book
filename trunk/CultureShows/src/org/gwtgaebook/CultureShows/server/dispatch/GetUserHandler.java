package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.*;

import com.google.inject.*;
import com.google.appengine.api.users.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;
import com.google.code.twig.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class GetUserHandler extends
		DispatchActionHandler<GetUserAction, GetUserResult> {

	@Inject
	public GetUserHandler(final ObjectDatastore datastore) {
		super(datastore);
	}

	@Override
	public GetUserResult execute(GetUserAction action, ExecutionContext context)
			throws ActionException {

		UserInfo userInfo = new UserInfo();

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		if (user != null) {
			userInfo.isSignedIn = true;
			userInfo.signOutURL = userService.createLogoutURL(action
					.getRequestURI());
			userInfo.email = user.getEmail();
			userInfo.userId = user.getUserId();
		} else {
			userInfo.isSignedIn = false;
			userInfo.signInURLs.put("Google", userService.createLoginURL(action
					.getRequestURI(), null, "google.com/accounts/o8/id", null));
			userInfo.signInURLs.put("Yahoo", userService.createLoginURL(action
					.getRequestURI(), null, "yahoo.com", null));
		}

		return new GetUserResult("", userInfo);
	}
}
