package org.gwtgaebook.CultureShows.server.dispatch;

import java.text.*;
import java.util.*;
import java.math.*;
import java.security.*;

import com.google.inject.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.shared.*;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.code.twig.*;
import com.google.code.twig.annotation.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class ScheduleShowHandler extends
		DispatchActionHandler<ScheduleShowAction, ScheduleShowResult> {

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
		System.out.println("Handler scheduling show " + action.getShowName());

		ObjectDatastore ds = new AnnotationObjectDatastore();

		String userId = "anonymous-asdf";

		Theater theater;
		Key theaterKey;
		if (null == action.getTheaterKey() || action.getTheaterKey().isEmpty()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			// String random = new BigInteger(130, new SecureRandom())
			// .toString(32);
			String random = UUID.randomUUID().toString();

			theater = new Theater();
			// TODO constructor
			theater.members = new HashSet<Member>();
			theater.shows = new HashSet<Show>();
			theater.locations = new HashSet<Location>();
			theater.schedule = new Schedule();

			theater.name = "anonymous-" + dateFormat.format(date) + "-"
					+ random;

			Member member = new Member();
			member.userId = userId;
			member.role = Member.Role.ADMINISTRATOR;
			theater.members.add(member);

			theaterKey = ds.store(theater);
			// theater = ds.load(Theater.class, theaterKey);
			// theater = ds
			// .load(Theater.class, KeyFactory.keyToString(theaterKey));

			ds.associate(theater, theaterKey);
			System.out.println("STORED theater " + theaterKey.toString());
		} else {
			// TODO try/catch invalid key
			theaterKey = KeyFactory.stringToKey(action.getTheaterKey());
			theater = ds.load(Theater.class, theaterKey);

		}

		System.out.println("Theater: " + theater.toString());
		// verify that userId has access to theaterKey

		Show show = new Show();
		show.name = action.getShowName();

		theater.shows.add(show);
		// ds.update(theater);
		ds.store().instance(show).parent(theater).now();

		//
		//
		// showKey = find(showName assigned to theaterKey)
		// if (showKey is empty) {
		// create and store show
		// }
		//
		// //same with location
		//
		// create schedule entry (showKey, locationKey, date) assigned to
		// theaterKey
		//
		// return theaterKey, showKey, locationKey
		//
		// Key showKey = ds.store(show, KeyFactory.keyToString(theaterKey));

		// System.out.println("showKey " + KeyFactory.keyToString(showKey));

		// TODO check if theater already exists; if not, create one first
		// TODO check if show already exists for this theater; if not, create
		// one first
		// TODO to improve performance, can we pass keys directly from client?
		// Need to check user has right to access that data first
		// TODO don't allow storing shows which aren't part of a theater?

		// TODO check if user has right to given theater first

		// TODO return Schedule model and key
		return new ScheduleShowResult(KeyFactory.keyToString(theaterKey));
	}
}
