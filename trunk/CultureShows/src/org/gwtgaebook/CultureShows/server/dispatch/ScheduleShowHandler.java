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

		Theater theater = null;
		Key theaterKey = null;

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
			theater = ds.load(theaterKey);
			if (null == theater) {
				theaterKey = null;
			} else {
				// verify userId has access to theater
				// TODO
			}
		}

		if (null == theaterKey) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date();
			String random = UUID.randomUUID().toString();

			theater = new Theater();

			theater.name = "anonymous-" + dateFormat.format(date) + "-"
					+ random;

			Member member = new Member();
			member.userId = userId;
			member.role = Member.Role.ADMINISTRATOR;
			theater.members.add(member);

			theaterKey = ds.store(theater);
			// store creates a Key in the datastore and keeps it in the
			// ObjectDatastore associated with this theater instance. Basically,
			// every OD has a Map<Object, Key> which is used to look up the Key
			// for every operation.

			System.out.println("STORED theatetr "
					+ KeyFactory.keyToString(theaterKey));
		}

		System.out.println("Current theater "
				+ KeyFactory.keyToString(theaterKey));

		Show show = new Show();
		show.name = action.getShowName();

		theater.shows.add(show);
		ds.update(theater);

		// TODO check if show already exists for this theater; if not, create
		// one first
		// TODO to improve performance, can we pass keys directly from client?
		// Need to check user has right to access that data first
		// TODO don't allow storing shows which aren't part of a theater?

		// TODO return Schedule model and key
		return new ScheduleShowResult(KeyFactory.keyToString(theaterKey));
	}
}
