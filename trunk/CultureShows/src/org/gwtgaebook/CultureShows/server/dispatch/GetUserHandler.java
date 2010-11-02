package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.*;

import com.google.inject.*;
import com.google.appengine.api.datastore.Key;
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

		// theaters user has access to; key/name
		List<Theater> theaters = new ArrayList<Theater>();

		if (userInfo.isSignedIn) {
			userInfo.signOutURL = userService.createLogoutURL(action
					.getRequestURI());

			// check if user has a Member record, create one if not
			Member member = null;
			Key memberKey = null;

			List<Member> members = datastore.find().type(Member.class)
					.addFilter("userId", FilterOperator.EQUAL, userInfo.userId)
					.returnAll().now();
			if (members.size() > 0) {
				// TODO log error if size != 1
				member = members.get(0);
				memberKey = datastore.associatedKey(member);

				// get theaters member has access to
				List<TheaterMemberJoin> tmjs = datastore
						.find()
						.type(TheaterMemberJoin.class)
						.addFilter("memberKey", FilterOperator.EQUAL,
								KeyFactory.keyToString(memberKey)).returnAll()
						.now();
				for (int i = 0; i < tmjs.size(); i++) {
					Theater t = new Theater();
					t.theaterKey = tmjs.get(i).theaterKey;
					t.name = tmjs.get(i).theaterName;
					theaters.add(t);
				}

			} else {
				// store member
				member = new Member();
				member.userId = userInfo.userId;
				member.email = userInfo.email;
				member.signUpDate = new Date();
				memberKey = datastore.store(member);
			}

		} else {
			userInfo.signInURLs.put("Google", userService.createLoginURL(
					action.getRequestURI(), null, "google.com/accounts/o8/id",
					null));
			userInfo.signInURLs.put("Yahoo", userService.createLoginURL(
					action.getRequestURI(), null, "yahoo.com", null));
		}

		return new GetUserResult("", userInfo, theaters);
	}
}
