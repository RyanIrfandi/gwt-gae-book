package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.locations.dispatch.ReadLocationsHandler;
import org.gwtgaebook.CultureShows.client.shows.dispatch.ReadShowsHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RESTHandlerRegistry extends DefaultClientActionHandlerRegistry {

	@Inject
	public RESTHandlerRegistry(final ReadLocationsHandler readLocationsHandler,
			final ReadShowsHandler readShowsHandler) {

		register(readShowsHandler);
		register(readLocationsHandler);
	}
}