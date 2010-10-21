package org.gwtgaebook.CultureShows.server.dispatch;

import java.text.*;
import java.util.*;

import com.google.inject.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.repackaged.com.google.common.base.Strings;
import com.google.code.twig.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.*;
import org.gwtgaebook.CultureShows.shared.model.*;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin.Role;

public class ScheduleShowHandler extends
		DispatchActionHandler<ScheduleShowAction, ScheduleShowResult> {

	@Inject
	public ScheduleShowHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public ScheduleShowResult execute(ScheduleShowAction action,
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

		String userId = null;
		Boolean anonymous = true;

		logger.info("Handler scheduling show " + action.getShowName());

		if (!userInfo.isSignedIn) {
			// TODO ActionValidator
			return new ScheduleShowResult("Not signed in", null, null);
		}

		// load member record, exception if it does not exist
		// TODO setup a reusable, testable provider for this
		List<Member> members = datastore.find().type(Member.class).addFilter(
				"userId", FilterOperator.EQUAL, userInfo.userId).returnAll()
				.now();
		if (members.size() > 0) {
			// TODO log error if size != 1
			member = members.get(0);
			memberKey = datastore.associatedKey(member);
		} else {
			return new ScheduleShowResult("Member doesn't exist", null, null);
		}

		// setup theater
		if (!Strings.isNullOrEmpty(action.getTheaterKey())) {
			// theaterKey sent by client is not empty
			try {
				theaterKey = KeyFactory.stringToKey(action.getTheaterKey());
			} catch (Exception e) {
				// invalid key, ignore it
			}
		}

		if (null != theaterKey) {
			theater = datastore.load(theaterKey);
			if (null == theater) {
				theaterKey = null;
			} else {
				// verify member has access to theater with role allowing
				// performance scheduling
				List<TheaterMemberJoin> tmjs = datastore.find().type(
						TheaterMemberJoin.class).addFilter("theaterKey",
						FilterOperator.EQUAL,
						KeyFactory.keyToString(theaterKey)).addFilter(
						"memberKey", FilterOperator.EQUAL,
						KeyFactory.keyToString(memberKey)).returnAll().now();
				if (tmjs.size() > 0) {
					// has access
				} else {
					return new ScheduleShowResult(
							"You don't have access to this theater", null, null);
				}

			}
		}

		if (null == theaterKey) {
			theater = new Theater();

			theater.name = Constants.defaultTheaterName;
			// store creates a Key in the datastore and keeps it in the
			// ObjectDatastore associated with this theater instance. Basically,
			// every OD has a Map<Object, Key> which is used to look up the Key
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

		logger.info("Current member " + KeyFactory.keyToString(memberKey));
		logger.info("Current theater " + KeyFactory.keyToString(theaterKey));

		// setup show
		// does show already exist?
		show = new Show();
		show.setName(action.getShowName());

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
		location.setName(action.getLocationName());

		List<Location> locations = datastore.find().type(Location.class)
				.ancestor(theater).addFilter("nameQuery", FilterOperator.EQUAL,
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

		// setup performance
		performance = new Performance();

		performance.date = action.getDate();
		performance.showKey = KeyFactory.keyToString(showKey);
		performance.locationKey = KeyFactory.keyToString(locationKey);

		performance.theaterKey = KeyFactory.keyToString(theaterKey);
		performance.showName = show.getName();
		performance.showWebsiteURL = show.websiteURL;
		performance.locationName = location.getName();
		performanceKey = datastore.store(performance);

		Map<String, Performance> performancesMap = new HashMap<String, Performance>();
		performancesMap
				.put(KeyFactory.keyToString(performanceKey), performance);

		// TODO to improve performance, can we pass show/location keys directly
		// from client?

		// TODO testability, break in smaller methods

		return new ScheduleShowResult("", KeyFactory.keyToString(theaterKey),
				performancesMap);
	}
}
