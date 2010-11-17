package org.gwtgaebook.CultureShows.server;

import com.gwtplatform.dispatch.server.guice.*;
import com.google.code.twig.*;
import com.google.code.twig.annotation.*;
import com.google.code.twig.configuration.*;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;
import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.MemberDAO;
import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterDAO;
import org.gwtgaebook.CultureShows.server.dao.TheaterMemberJoinDAO;
import org.gwtgaebook.CultureShows.server.dispatch.*;

public class MainHandlerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bind(ObjectDatastore.class).toProvider(
				AnnotationObjectDatastoreProvider.class).in(Singleton.class);
		bind(UserInfo.class).toProvider(UserInfoProvider.class);

		bind(TheaterDAO.class);
		bind(ShowDAO.class);
		bind(LocationDAO.class);
		bind(PerformanceDAO.class);
		bind(MemberDAO.class);
		bind(TheaterMemberJoinDAO.class);

		// bind Actions to ActionHandlers and ActionValidators
		bindHandler(ManagePerformanceAction.class,
				ManagePerformanceHandler.class);
		bindHandler(GetPerformancesAction.class, GetPerformancesHandler.class);
		bindHandler(GetUserAction.class, GetUserHandler.class);

	}
}
