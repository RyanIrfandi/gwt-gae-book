package org.gwtgaebook.CultureShows.server.api;

import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.inject.Inject;

public class ShowsResource extends ServerResource {

	@Inject
	ShowDAO showDAO;
	@Inject
	Gson gson;

	public class ShowsGET {
		@Expose
		List<Show> shows;
	}

	@Get("json")
	public Representation get() {
		ShowsGET get = new ShowsGET();
		get.shows = showDAO.readByTheater((String) getRequestAttributes().get(
				"theaterKey"));
		JsonRepresentation representation = new JsonRepresentation(
				gson.toJson(get));

		return representation;
	}
}
