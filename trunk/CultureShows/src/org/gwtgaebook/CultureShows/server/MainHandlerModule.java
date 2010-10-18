package org.gwtgaebook.CultureShows.server;

import com.gwtplatform.dispatch.server.guice.*;
import com.google.code.twig.*;
import com.google.code.twig.annotation.*;
import com.google.code.twig.configuration.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;
import org.gwtgaebook.CultureShows.server.dispatch.*;

public class MainHandlerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bind(ObjectDatastore.class).toProvider(
				AnnotationObjectDatastoreProvider.class);
		// bind(ObjectDatastore.class).to(AnnotationObjectDatastore.class);

		// bind Actions to ActionHandlers and ActionValidators
		bindHandler(ScheduleShowAction.class, ScheduleShowHandler.class);
		bindHandler(GetPerformancesAction.class, GetPerformancesHandler.class);
		bindHandler(GetUserAction.class, GetUserHandler.class);

		// datastore config
		DefaultConfiguration.registerTypeName(Member.class, "Member");
		DefaultConfiguration.registerTypeName(Theater.class, "Theater");
		DefaultConfiguration.registerTypeName(TheaterMemberJoin.class,
				"TheaterMemberJoin");
		DefaultConfiguration.registerTypeName(Show.class, "Show");
		DefaultConfiguration.registerTypeName(Location.class, "Location");
		DefaultConfiguration.registerTypeName(Performance.class, "Performance");

	}
}
