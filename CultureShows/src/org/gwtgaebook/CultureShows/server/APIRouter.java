package org.gwtgaebook.CultureShows.server;

import name.pehl.taoki.GuiceRouter;

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
		attach("/theaters/{id}/shows", ShowsResource.class);
		attach("/theaters/{id}/locations", LocationsResource.class);
		// TODO
		attach("/theaters/{id}/locations/", LocationsResource.class);
		attach("/theaters/{id}/performances", PerformancesResource.class);
	}
}
