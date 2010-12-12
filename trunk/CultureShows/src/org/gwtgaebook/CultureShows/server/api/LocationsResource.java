package org.gwtgaebook.CultureShows.server.api;

import java.util.List;

import org.gwtgaebook.CultureShows.server.api.ShowsResource.ShowsGET;
import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.inject.Inject;

public class LocationsResource extends ServerResource {

	@Inject
	LocationDAO locationDAO;
	@Inject
	Gson gson;

	public class LocationsGET {
		@Expose
		List<Location> locations;
	}

	@Get("json")
	public Representation get() {
		LocationsGET get = new LocationsGET();
		get.locations = locationDAO
				.readByTheater((String) getRequestAttributes().get("id"));
		JsonRepresentation representation = new JsonRepresentation(
				gson.toJson(get));

		return representation;
	}
}
