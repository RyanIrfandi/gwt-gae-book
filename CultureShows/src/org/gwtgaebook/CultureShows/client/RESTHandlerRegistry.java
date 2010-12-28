package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.locations.dispatch.CreateLocationHandler;
import org.gwtgaebook.CultureShows.client.locations.dispatch.DeleteLocationHandler;
import org.gwtgaebook.CultureShows.client.locations.dispatch.ReadLocationsHandler;
import org.gwtgaebook.CultureShows.client.locations.dispatch.UpdateLocationHandler;
import org.gwtgaebook.CultureShows.client.shows.dispatch.ReadShowsHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RESTHandlerRegistry extends DefaultClientActionHandlerRegistry {

	@Inject
	public RESTHandlerRegistry(final ReadLocationsHandler readLocationsHandler,
			final ReadShowsHandler readShowsHandler,
			final CreateLocationHandler createLocationHandler,
			final DeleteLocationHandler deleteLocationHandler,
			final UpdateLocationHandler updateLocationHandler) {

		register(readShowsHandler);
		register(readLocationsHandler);
		register(createLocationHandler);
		register(deleteLocationHandler);
		register(updateLocationHandler);
	}
}