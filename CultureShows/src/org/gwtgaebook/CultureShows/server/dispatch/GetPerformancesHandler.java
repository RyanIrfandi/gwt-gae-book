package org.gwtgaebook.CultureShows.server.dispatch;

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
import org.gwtgaebook.CultureShows.shared.model.*;

public class GetPerformancesHandler extends
		DispatchActionHandler<GetPerformancesAction, GetPerformancesResult> {

	@Inject
	public GetPerformancesHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetPerformancesResult execute(GetPerformancesAction action,
			ExecutionContext context) throws ActionException {

		Key theaterKey = null;

		// by default, get only future performances
		Date date = new java.util.Date();

		// check key
		if (!Strings.isNullOrEmpty(action.getTheaterKey())) {
			// theaterKey sent by client is not empty
			try {
				theaterKey = KeyFactory.stringToKey(action.getTheaterKey());
			} catch (Exception e) {
				// invalid key, ignore it
			}
		}

		if (null == theaterKey) {
			return new GetPerformancesResult("Empty or invalid theater", null);
		}

		// no need to check access, performances are public

		// query for shows belonging to a theater instance
		List<Performance> performances = datastore.find().type(
				Performance.class).addFilter("theaterKey",
				FilterOperator.EQUAL, KeyFactory.keyToString(theaterKey))
				.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, date)
				.addSort("date").returnAll().now();

		// add key to model, so it can be sent to client
		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.performanceKey = KeyFactory.keyToString(datastore
					.associatedKey(p));
			performances.set(i, p);
		}

		return new GetPerformancesResult("", performances);
	}
}
