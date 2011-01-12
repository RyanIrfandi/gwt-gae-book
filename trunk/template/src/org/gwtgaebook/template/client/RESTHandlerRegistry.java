package org.gwtgaebook.template.client;

import org.gwtgaebook.template.client.locations.dispatch.CreateLocationHandler;
import org.gwtgaebook.template.client.locations.dispatch.DeleteLocationHandler;
import org.gwtgaebook.template.client.locations.dispatch.ReadLocationsHandler;
import org.gwtgaebook.template.client.locations.dispatch.UpdateLocationHandler;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.actionhandler.DefaultClientActionHandlerRegistry;

public class RESTHandlerRegistry extends DefaultClientActionHandlerRegistry {

	@Inject
	public RESTHandlerRegistry(final ReadLocationsHandler readLocationsHandler,
			final CreateLocationHandler createLocationHandler,
			final DeleteLocationHandler deleteLocationHandler,
			final UpdateLocationHandler updateLocationHandler) {

		register(readLocationsHandler);
		register(createLocationHandler);
		register(deleteLocationHandler);
		register(updateLocationHandler);
	}
}