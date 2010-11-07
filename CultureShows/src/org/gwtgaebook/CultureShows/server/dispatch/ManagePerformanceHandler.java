package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.List;

import org.gwtgaebook.CultureShows.server.util.Validation;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.Constants.ManageActionType;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceResult;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin.Role;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

// handle CUD action types
public class ManagePerformanceHandler extends
		DispatchActionHandler<ManagePerformanceAction, ManagePerformanceResult> {

	@Inject
	public ManagePerformanceHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public ManagePerformanceResult execute(ManagePerformanceAction action,
			ExecutionContext context) throws ActionException {

		UserInfo userInfo = userInfoProvider.get();

		// TODO Verify that the input is valid.
		// if (!FieldVerifier.isValidName(input)) {
		// // If the input is not valid, throw an IllegalArgumentException
		// // back to
		// // the client.
		// throw new ActionException(
		// "Name must be at least 4 characters long");
		// }
		Member member = null;
		Key memberKey = null;

		Theater theater = null;
		Key theaterKey = null;

		TheaterMemberJoin tmj = null;

		Key showKey = null;
		Show show = null;

		Key locationKey = null;
		Location location = null;

		Key performanceKey = null;
		Performance performance = null;

		if (!userInfo.isSignedIn) {
			// TODO ActionValidator
			return new ManagePerformanceResult("Not signed in", null);
		}

		// load member record, exception if it does not exist
		// TODO setup a reusable, testable provider for this
		List<Member> members = datastore.find().type(Member.class)
				.addFilter("userId", FilterOperator.EQUAL, userInfo.userId)
				.returnAll().now();
		if (members.size() > 0) {
			// TODO log error if size != 1
			member = members.get(0);
			memberKey = datastore.associatedKey(member);
		} else {
			return new ManagePerformanceResult("Member doesn't exist", null);
		}

		// setup theater

		switch (action.getActionType()) {
		case CREATE:
			if (!Strings.isNullOrEmpty(action.getPerformance().theaterKey)) {
				// theaterKey sent by client is not empty
				theaterKey = Validation
						.getValidDSKey(action.getPerformance().theaterKey);
			}
			break;

		case UPDATE:
		case DELETE:
			theaterKey = Validation
					.getValidDSKey(action.getPerformance().theaterKey);
			break;
		default:
			return new ManagePerformanceResult("Invalid action type: "
					+ action.getActionType(), null);
		}

		if (null != theaterKey) {
			theater = datastore.load(theaterKey);
			if (null == theater) {
				theaterKey = null;
			} else {
				// verify member has access to theater with role allowing
				// managing performances
				List<TheaterMemberJoin> tmjs = datastore
						.find()
						.type(TheaterMemberJoin.class)
						.addFilter("theaterKey", FilterOperator.EQUAL,
								KeyFactory.keyToString(theaterKey))
						.addFilter("memberKey", FilterOperator.EQUAL,
								KeyFactory.keyToString(memberKey)).returnAll()
						.now();
				if (tmjs.size() > 0) {
					// has access
				} else {
					return new ManagePerformanceResult(
							"You don't have access to this theater", null);
				}

			}
		}

		switch (action.getActionType()) {
		case CREATE:
			// create theater instance
			if (null == theaterKey) {
				theater = new Theater();

				theater.name = Constants.defaultTheaterName;
				// store creates a Key in the datastore and keeps it in the
				// ObjectDatastore associated with this theater instance.
				// Basically,
				// every OD has a Map<Object, Key> which is used to look up the
				// Key
				// for every operation.
				theaterKey = datastore.store(theater);

				// assign member to theater
				tmj = new TheaterMemberJoin();
				tmj.theaterKey = KeyFactory.keyToString(theaterKey);
				tmj.memberKey = KeyFactory.keyToString(memberKey);
				tmj.role = Role.ADMINISTRATOR;

				tmj.theaterName = theater.name;
				tmj.memberEmail = member.email;
				tmj.memberName = member.name;

				datastore.store(tmj);
			}

			performance = action.getPerformance();
			break;

		case UPDATE:
		case DELETE:
			performanceKey = Validation
					.getValidDSKey(action.getPerformance().performanceKey);

			performance = datastore.load(performanceKey);

			// check performance belongs to given theaterKey, exception if not
			if (!performance.theaterKey.equals(KeyFactory
					.keyToString(theaterKey))) {
				return new ManagePerformanceResult(
						"Performance doesn't belong to given Theater", null);
			}
			break;

		default:
			return new ManagePerformanceResult("Invalid action type: "
					+ action.getActionType(), null);
		}

		logger.info("Current member " + KeyFactory.keyToString(memberKey));
		logger.info("Current theater " + KeyFactory.keyToString(theaterKey));

		if (action.getActionType() == ManageActionType.DELETE) {
			datastore.delete(performance);
			return new ManagePerformanceResult("", null);
		}

		// setup show
		// does show already exist?
		show = new Show();
		show.setName(action.getPerformance().showName);

		List<Show> shows = datastore.find().type(Show.class).ancestor(theater)
				.addFilter("nameQuery", FilterOperator.EQUAL, show.nameQuery)
				.returnAll().now();
		if (shows.size() > 0) {
			// TODO log error if size != 1
			show = shows.get(0);
			showKey = datastore.associatedKey(show);
		} else {
			// store show belonging to a theater
			showKey = datastore.store().instance(show).parent(theater).now();
		}
		logger.info("Current show " + KeyFactory.keyToString(showKey));

		// setup location
		// does location already exist?
		location = new Location();
		location.setName(action.getPerformance().locationName);

		List<Location> locations = datastore
				.find()
				.type(Location.class)
				.ancestor(theater)
				.addFilter("nameQuery", FilterOperator.EQUAL,
						location.nameQuery).returnAll().now();
		if (locations.size() > 0) {
			// TODO log error if size != 1
			location = locations.get(0);
			locationKey = datastore.associatedKey(location);
		} else {
			// store show belonging to a theater
			locationKey = datastore.store().instance(location).parent(theater)
					.now();
		}
		logger.info("Current location " + KeyFactory.keyToString(locationKey));

		// set/update performance data
		performance.date = action.getPerformance().date;
		performance.showKey = KeyFactory.keyToString(showKey);
		performance.locationKey = KeyFactory.keyToString(locationKey);

		performance.theaterKey = KeyFactory.keyToString(theaterKey);
		performance.showName = show.getName();
		performance.showWebsiteURL = show.websiteURL;
		performance.locationName = location.getName();
		datastore.storeOrUpdate(performance);

		performanceKey = datastore.associatedKey(performance);
		performance.performanceKey = KeyFactory.keyToString(performanceKey);

		// TODO testability, break in smaller methods

		return new ManagePerformanceResult("", performance);
	}
}
