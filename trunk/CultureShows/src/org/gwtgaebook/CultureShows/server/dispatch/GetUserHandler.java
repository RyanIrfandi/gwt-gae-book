package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserResult;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin.Role;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetUserHandler extends
		DispatchActionHandler<GetUserAction, GetUserResult> {

	@Inject
	MemberDAO memberDAO;
	@Inject
	TheaterDAO theaterDAO;
	@Inject
	TheaterMemberJoinDAO tmjDAO;

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

			member = memberDAO.readByUserId(userInfo.userId);

			if (null == member) {
				// first signin, create new member
				member = new Member();
				member.userId = userInfo.userId;
				member.email = userInfo.email;
				member.signUpDate = new Date();
				memberDAO.create(member);

			}
			memberKey = memberDAO.getKey(member);

			theaters = tmjDAO.readbyMember(KeyFactory.keyToString(memberKey));

			if (0 == theaters.size()) {
				// create a theater and give member access to it
				Theater theater = new Theater();
				theater.name = Constants.defaultTheaterName;
				Key theaterKey = theaterDAO.create(theater);
				theater.theaterKey = KeyFactory.keyToString(theaterKey);
				theaters.add(theater);

				// assign member to theater
				TheaterMemberJoin tmj = new TheaterMemberJoin();
				tmj.theaterKey = KeyFactory.keyToString(theaterKey);
				tmj.memberKey = KeyFactory.keyToString(memberKey);
				tmj.role = Role.ADMINISTRATOR;

				tmj.theaterName = theater.name;
				tmj.memberEmail = member.email;
				tmj.memberName = member.name;

				tmjDAO.create(tmj);
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
