package org.gwtgaebook.CultureShows.server;

import name.pehl.taoki.GuiceRouter;

import org.gwtgaebook.CultureShows.server.api.LocationResource;
import org.gwtgaebook.CultureShows.server.api.LocationsResource;
import org.gwtgaebook.CultureShows.server.api.PerformancesResource;
import org.gwtgaebook.CultureShows.server.api.ShowsResource;
import org.restlet.Context;

import com.google.inject.Injector;

public class APIRouter extends GuiceRouter {
	public APIRouter(Injector injector, Context context) {
		super(injector, context);
		// setRoutingMode(MODE_LAST_MATCH);
	}

	@Override
	protected void attachRoutes() {
		// TODO remove dupe routes
		// http://restlet.tigris.org/issues/show_bug.cgi?id=1220
		// http://restlet.tigris.org/ds/viewMessage.do?dsForumId=4447&dsMessageId=2689280
		attach("/theaters/{theaterKey}/shows", ShowsResource.class);
		attach("/theaters/{theaterKey}/shows/", ShowsResource.class);
		attach("/theaters/{theaterKey}/locations", LocationsResource.class);
		attach("/theaters/{theaterKey}/locations/", LocationsResource.class);
		attach("/theaters/{theaterKey}/locations/{locationKey}",
				LocationResource.class);
		attach("/theaters/{theaterKey}/locations/{locationKey}/",
				LocationResource.class);
		attach("/theaters/{theaterKey}/performances",
				PerformancesResource.class);
	}
}
