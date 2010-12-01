package org.gwtgaebook.CultureShows.server;

import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.server.dispatch.GetPerformancesHandler;
import org.gwtgaebook.CultureShows.server.dispatch.GetShowsHandler;
import org.gwtgaebook.CultureShows.server.dispatch.GetUserHandler;
import org.gwtgaebook.CultureShows.server.dispatch.GetUserSampleHandler;
import org.gwtgaebook.CultureShows.server.dispatch.ManagePerformanceHandler;
import org.gwtgaebook.CultureShows.server.dispatch.ManageShowHandler;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetShowsAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserSampleAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManageShowAction;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.code.twig.ObjectDatastore;
import com.google.gson.Gson;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.server.guice.HandlerModule;

public class MainHandlerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bind(ObjectDatastore.class).toProvider(
				AnnotationObjectDatastoreProvider.class).in(Singleton.class);
		bind(UserInfo.class).toProvider(UserInfoProvider.class);
		bind(Gson.class).toProvider(GsonProvider.class);

		bind(TheaterDAO.class);
		bind(ShowDAO.class);
		bind(LocationDAO.class);
		bind(PerformanceDAO.class);
		bind(MemberDAO.class);
		bind(TheaterMemberJoinDAO.class);

		// bind Actions to ActionHandlers and ActionValidators
		bindHandler(GetUserSampleAction.class, GetUserSampleHandler.class);
		bindHandler(GetUserAction.class, GetUserHandler.class);
		bindHandler(GetPerformancesAction.class, GetPerformancesHandler.class);
		bindHandler(ManagePerformanceAction.class,
				ManagePerformanceHandler.class);
		bindHandler(GetShowsAction.class, GetShowsHandler.class);
		bindHandler(ManageShowAction.class, ManageShowHandler.class);

	}
}
