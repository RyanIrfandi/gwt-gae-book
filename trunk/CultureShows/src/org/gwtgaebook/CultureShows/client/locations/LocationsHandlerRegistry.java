package org.gwtgaebook.CultureShows.client.locations;

import org.gwtgaebook.CultureShows.client.locations.dispatch.ReadLocationsHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class LocationsHandlerRegistry extends
		DefaultClientActionHandlerRegistry {

	@Inject
	public LocationsHandlerRegistry(
			final ReadLocationsHandler readLocationsHandler) {

		register(readLocationsHandler);
	}
}