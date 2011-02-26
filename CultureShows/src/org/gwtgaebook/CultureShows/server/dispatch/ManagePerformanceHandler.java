package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.server.util.Validation;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceResult;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Member;
import org.gwtgaebook.CultureShows.shared.model.Performance;
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
public class ManagePerformanceHandler extends
		DispatchActionHandler<ManagePerformanceAction, ManagePerformanceResult> {

	@Inject
	MemberDAO memberDAO;
	@Inject
	TheaterMemberJoinDAO tmjDAO;
	@Inject
	TheaterDAO theaterDAO;
	@Inject
	ShowDAO showDAO;
	@Inject
	LocationDAO locationDAO;
	@Inject
	PerformanceDAO performanceDAO;

	@Inject
	public ManagePerformanceHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public ManagePerformanceResult execute(ManagePerformanceAction action,
			ExecutionContext context) throws ActionException {
		// if show or location do not exist yet, this will create them
		// TODO client should send show&location keys if they already exist;
		// check if are valid, permissions

		// TODO Verify that the input is valid.
		// if (!FieldVerifier.isValidName(input)) {
		// // If the input is not valid, throw an IllegalArgumentException
		// // back to
		// // the client.
		// throw new ActionException(
		// "Name must be at least 4 characters long");
		// }

		UserInfo userInfo = userInfoProvider.get();

		Member member = null;

		Theater theater = null;
		Key theaterKey = null;
		Show show = null;
		Location location = null;

		Key performanceKey = null;
		Performance performance = null;

		if (!userInfo.isSignedIn) {
			// TODO ActionValidator
			return new ManagePerformanceResult("Not signed in", null);
		}

		member = memberDAO.readByUserId(userInfo.userId);
		theaterKey = Validation.getValidDSKey(action.getTheaterKey());

		// TODO ActionValidator
		if (!tmjDAO.memberHasAccessToTheater(
				KeyFactory.keyToString(memberDAO.getKey(member)),
				KeyFactory.keyToString(theaterKey))) {
			return new ManagePerformanceResult(
					"You don't have access to this theater", null);

		}

		theater = theaterDAO.read(theaterKey);

		switch (action.getActionType()) {
		case CREATE:
			performance = action.getPerformance();
			show = createOrReadShow(theater, action.getPerformance().showName);
			location = createOrReadLocation(theater,
					action.getPerformance().locationName);

			// set/update performance data
			performance.showKey = KeyFactory.keyToString(showDAO.getKey(show));
			performance.locationKey = KeyFactory.keyToString(locationDAO
					.getKey(location));

			performance.showName = show.getName();
			performance.showWebsiteURL = show.websiteURL;
			performance.locationName = location.getName();
			performanceKey = performanceDAO.create(theater, performance);

			break;

		case UPDATE:
			performanceKey = Validation
					.getValidDSKey(action.getPerformance().performanceKey);

			// check performance belongs to given theaterKey
			if (!KeyFactory.keyToString(performanceKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManagePerformanceResult(
						"Performance doesn't belong to given theater", null);
			}

			performance = performanceDAO.read(performanceKey);

			show = createOrReadShow(theater, action.getPerformance().showName);
			location = createOrReadLocation(theater,
					action.getPerformance().locationName);

			// set/update performance data
			performance.date = action.getPerformance().date;
			performance.timeHourMinute = action.getPerformance().timeHourMinute;
			performance.showKey = KeyFactory.keyToString(showDAO.getKey(show));
			performance.locationKey = KeyFactory.keyToString(locationDAO
					.getKey(location));

			performance.showName = show.getName();
			performance.showWebsiteURL = show.websiteURL;
			performance.locationName = location.getName();

			performanceDAO.update(performance, performanceKey);

			break;

		case DELETE:
			performanceKey = Validation
					.getValidDSKey(action.getPerformance().performanceKey);

			// check performance belongs to given theaterKey
			if (!KeyFactory.keyToString(performanceKey.getParent()).equals(
					KeyFactory.keyToString(theaterKey))) {
				return new ManagePerformanceResult(
						"Performance doesn't belong to given theater", null);
			}
			performanceDAO.delete(performanceKey);
			return new ManagePerformanceResult("", null);

		default:
			return new ManagePerformanceResult("Invalid action type: "
					+ action.getActionType(), null);
		}

		performance.performanceKey = KeyFactory.keyToString(performanceKey);

		return new ManagePerformanceResult("", performance);
	}

	Show createOrReadShow(Theater theater, String showName) {
		// setup show
		// does show already exist?
		Show show = new Show();
		show.setName(showName);

		List<Show> shows = showDAO.readByName(theater, showName);
		if (shows.size() > 0) {
			show = shows.get(0);
		} else {
			showDAO.create(theater, show);
		}
		logger.info("Current show "
				+ KeyFactory.keyToString(showDAO.getKey(show)));

		return show;
	}

	Location createOrReadLocation(Theater theater, String locationName) {
		// setup location
		// does location already exist?
		Location location = new Location();
		location.setName(locationName);

		List<Location> locations = locationDAO
				.readByName(theater, locationName);
		if (locations.size() > 0) {
			location = locations.get(0);
		} else {
			locationDAO.create(theater, location);
		}

		logger.info("Current location "
				+ KeyFactory.keyToString(locationDAO.getKey(location)));

		return location;
	}

}
