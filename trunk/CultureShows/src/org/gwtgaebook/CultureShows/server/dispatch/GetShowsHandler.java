package org.gwtgaebook.CultureShows.server.dispatch;

import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.shared.dispatch.GetShowsAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetShowsResult;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetShowsHandler extends
		DispatchActionHandler<GetShowsAction, GetShowsResult> {

	@Inject
	ShowDAO showDAO;

	@Inject
	public GetShowsHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		super(userInfoProvider, datastore);
	}

	@Override
	public GetShowsResult execute(GetShowsAction action,
			ExecutionContext context) throws ActionException {

		// no need to check access, shows are public

		List<Show> shows = showDAO.readByTheater(action.getTheaterKey());

		return new GetShowsResult("", shows);
	}
}
