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

import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class GetPerformancesHandler extends
		DispatchActionHandler<GetPerformancesAction, GetPerformancesResult> {

	@Inject
	PerformanceDAO performanceDAO;

	@Inject
	public GetPerformancesHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetPerformancesResult execute(GetPerformancesAction action,
			ExecutionContext context) throws ActionException {

		// no need to check access, performances are public

		List<Performance> performances = performanceDAO.readByTheater(action
				.getTheaterKey());

		// add key to model, so it can be sent to client
		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.performanceKey = KeyFactory.keyToString(performanceDAO.getKey(p));
			performances.set(i, p);
		}

		return new GetPerformancesResult("", performances);
	}
}
