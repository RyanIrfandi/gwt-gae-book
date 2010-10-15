package org.gwtgaebook.CultureShows.server.dispatch;

import java.text.*;
import java.util.*;

import com.google.inject.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;
import org.gwtgaebook.CultureShows.shared.model.TheaterMemberJoin.Role;

public class ScheduleShowHandler extends
		DispatchActionHandler<ScheduleShowAction, ScheduleShowResult> {

	@Inject
	public ScheduleShowHandler(final ObjectDatastore datastore) {
		super(datastore);
	}

	@Override
	public ScheduleShowResult execute(ScheduleShowAction action,
			ExecutionContext context) throws ActionException {

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

		// authentication
		// TODO anonymous = (AppEngineUser is not signed in)

		if (anonymous) {
			userId = "anonymous-" + action.getUserToken();
			// prefix prevents client setting valid user IDs and overwriting
			// their data
			// issue: client can overwrite any anonymous user data by setting a
			// matching RandomToken. That's ok.
		} else {
			// TODO set userId
		}

		// setup member
		// does this member already exist?
		List<Member> members = datastore.find().type(Member.class).addFilter(
				"userId", FilterOperator.EQUAL, userId).returnAll().now();
		if (members.size() > 0) {
			// TODO log error if size != 1
			member = members.get(0);
			memberKey = datastore.associatedKey(member);
		} else {
			// store member
			// TODO if signed in, fill email, language...
			member = new Member();
			member.userId = userId;
			memberKey = datastore.store(member);

		}

		// setup theater
		if (!(null == action.getTheaterKey() || action.getTheaterKey()
				.isEmpty())) {
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
						FilterOperator.EQUAL, theaterKey).addFilter(
						"memberKey", FilterOperator.EQUAL, memberKey)
						.returnAll().now();
				if (tmjs.size() > 0) {
					// has access
				} else {
					return new ScheduleShowResult(
							"You don't have access to this theater", "");
				}

			}
		}

		if (null == theaterKey) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String random = UUID.randomUUID().toString();

			theater = new Theater();

			theater.name = "anonymous-" + dateFormat.format(date) + "-"
					+ random;
			// store creates a Key in the datastore and keeps it in the
			// ObjectDatastore associated with this theater instance. Basically,
			// every OD has a Map<Object, Key> which is used to look up the Key
			// for every operation.
			theaterKey = datastore.store(theater);

			// assign member to theater
			tmj = new TheaterMemberJoin();
			tmj.theaterKey = theaterKey;
			tmj.memberKey = memberKey;
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

		// query for shows belonging to a theater instance
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
		// does show already exist?
		location = new Location();
		location.setName(action.getLocationName());

		// query for shows belonging to a theater instance
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
		performance.showKey = showKey;
		performance.locationKey = locationKey;

		performance.theaterKey = theaterKey;
		performance.showName = show.getName();
		performance.showWebsiteURL = show.websiteURL;
		performance.locationName = location.getName();
		performanceKey = datastore.store(performance);

		// TODO to improve performance, can we pass keys directly from client?

		// TODO testability, break in smaller methods

		// TODO return Performance model and key?
		return new ScheduleShowResult("", KeyFactory.keyToString(theaterKey));
	}
}
