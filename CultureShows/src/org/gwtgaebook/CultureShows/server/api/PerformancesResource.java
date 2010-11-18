package org.gwtgaebook.CultureShows.server.api;

import java.lang.reflect.Modifier;
import java.util.List;

import org.gwtgaebook.CultureShows.server.dao.LocationDAO;
import org.gwtgaebook.CultureShows.server.dao.PerformanceDAO;
import org.gwtgaebook.CultureShows.server.dao.ShowDAO;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

//ServerResource
public class PerformancesResource extends ServerResource {

	@Inject
	PerformanceDAO performanceDAO;
	@Inject
	ShowDAO showDAO;
	@Inject
	LocationDAO locationDAO;

	@Get("json")
	public Representation get() {
		List<Performance> performances = performanceDAO
				.readByTheater((String) getRequestAttributes().get("id"));

		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.show = showDAO.read(p.showKey);
			p.location = locationDAO.read(p.locationKey);
			performances.set(i, p);
		}

		// TODO inject
		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC)
				.excludeFieldsWithoutExposeAnnotation().create();
		JsonRepresentation representation = new JsonRepresentation(
				gson.toJson(performances));

		return representation;
	}

}
