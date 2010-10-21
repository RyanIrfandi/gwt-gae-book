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
		Map<String, Performance> performancesMap = new HashMap<String, Performance>();

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
				.returnAll().now();
		for (int i = 0; i < performances.size(); i++) {
			performancesMap.put(KeyFactory.keyToString(datastore
					.associatedKey(performances.get(i))), performances.get(i));
		}

		return new GetPerformancesResult("", performancesMap);
	}
}
