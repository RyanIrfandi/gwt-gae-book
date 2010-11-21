package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.server.util.Validation;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowResult;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowResult;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

// handle CUD action types
public class ManageShowHandler extends
		DispatchActionHandler<ManageShowAction, ManageShowResult> {

	@Inject
	MemberDAO memberDAO;
	@Inject
	TheaterMemberJoinDAO tmjDAO;
	@Inject
	TheaterDAO theaterDAO;
	@Inject
	ShowDAO showDAO;

	@Inject
	public ManageShowHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public ManageShowResult execute(ManageShowAction action,
			ExecutionContext context) throws ActionException {
		// TODO Verify that the input is valid.

		UserInfo userInfo = userInfoProvider.get();

		Member member = null;

		Theater theater = null;
		Key theaterKey = null;
		Show show = null;
		Location location = null;

		Key showKey = null;

		if (!userInfo.isSignedIn) {
			// TODO ActionValidator
			return new ManageShowResult("Not signed in", null);
		}

		member = memberDAO.readByUserId(userInfo.userId);
		theaterKey = Validation.getValidDSKey(action.getTheaterKey());

		// TODO ActionValidator
		if (!tmjDAO.memberHasAccessToTheater(
				KeyFactory.keyToString(memberDAO.getKey(member)),
				KeyFactory.keyToString(theaterKey))) {
			return new ManageShowResult(
					"You don't have access to this theater", null);

		}

		theater = theaterDAO.read(theaterKey);

		switch (action.getActionType()) {
		case CREATE:
			show = action.getShow();
			showKey = showDAO.create(theater, show);

			break;

		case UPDATE:
			showKey = Validation.getValidDSKey(action.getShow().showKey);

			// check show belongs to given theaterKey
			if (!KeyFactory.keyToString(showKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManageShowResult(
						"Show doesn't belong to given theater", null);
			}

			show = showDAO.read(showKey);

			// set/update show data
			show.setName(action.getShow().getName());
			show.websiteURL = action.getShow().websiteURL;
			show.minuteDuration = action.getShow().minuteDuration;
			show.posterURL = action.getShow().posterURL;

			showDAO.update(show, showKey);

			break;

		case DELETE:
			showKey = Validation.getValidDSKey(action.getShow().showKey);

			// check show belongs to given theaterKey
			if (!KeyFactory.keyToString(showKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManageShowResult(
						"Show doesn't belong to given theater", null);
			}
			showDAO.delete(showKey);
			return new ManageShowResult("", null);

		default:
			return new ManageShowResult("Invalid action type: "
					+ action.getActionType(), null);
		}

		show.showKey = KeyFactory.keyToString(showKey);

		return new ManageShowResult("", show);
	}

}
