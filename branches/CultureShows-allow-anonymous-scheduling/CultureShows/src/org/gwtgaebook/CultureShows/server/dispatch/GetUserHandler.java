package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.*;

import com.google.inject.*;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.users.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;
import com.google.code.twig.*;

import org.gwtgaebook.CultureShows.server.UserInfoProvider;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class GetUserHandler extends
		DispatchActionHandler<GetUserAction, GetUserResult> {

	@Inject
	public GetUserHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetUserResult execute(GetUserAction action, ExecutionContext context)
			throws ActionException {

		UserInfo userInfo = userInfoProvider.get();

		UserService userService = UserServiceFactory.getUserService();

		if (userInfo.isSignedIn) {
			userInfo.signOutURL = userService.createLogoutURL(action
					.getRequestURI());
		} else {
			userInfo.signInURLs.put("Google", userService.createLoginURL(action
					.getRequestURI(), null, "google.com/accounts/o8/id", null));
			userInfo.signInURLs.put("Yahoo", userService.createLoginURL(action
					.getRequestURI(), null, "yahoo.com", null));
		}

		return new GetUserResult("", userInfo);
	}
}
