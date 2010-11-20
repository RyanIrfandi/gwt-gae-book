package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesResult;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

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

		return new GetPerformancesResult("", performances);
	}
}
