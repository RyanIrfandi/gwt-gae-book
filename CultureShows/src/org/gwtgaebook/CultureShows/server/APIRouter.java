package org.gwtgaebook.CultureShows.server;

import name.pehl.taoki.GuiceRouter;

import org.gwtgaebook.CultureShows.server.api.PerformancesResource;
import org.restlet.Context;

import com.google.inject.Injector;

public class APIRouter extends GuiceRouter {
	public APIRouter(Injector injector, Context context) {
		super(injector, context);
	}

	@Override
	protected void attachRoutes() {
		attach("/theaters/{id}/performances", PerformancesResource.class);
		// attach("/performances", PerformancesResource.class);
	}
}
