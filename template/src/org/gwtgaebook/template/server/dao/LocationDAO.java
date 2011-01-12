package org.gwtgaebook.template.server.dao;

import java.util.List;

import org.gwtgaebook.template.server.model.Location;
import org.gwtgaebook.template.server.model.Theater;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class LocationDAO extends DAO<Location> {
	@Inject
	Provider<TheaterDAO> theaterDAOProvider;

	@Inject
	public LocationDAO(final ObjectDatastore datastore) {
		super(Location.class, datastore);
	}

	@Override
	public Key create(Location location) {
		throw new IllegalArgumentException("Location " + location.getName()
				+ " is not associated to a theater");
	}

	public Key create(Theater theater, Location location) {
		if (null == theater) {
			throw new IllegalArgumentException("Theater for location "
					+ location.getName() + " is invalid");
		}
		return datastore.store().instance(location).parent(theater).now();
	}

	public List<Location> readByTheater(String theaterKey) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		Theater theater = theaterDAO.read(theaterKey);

		List<Location> locations = datastore.find().type(Location.class)
				.ancestor(theater).returnAll().now();

		// set key
		Location s;
		for (int i = 0; i < locations.size(); i++) {
			s = locations.get(i);
			s.locationKey = KeyFactory.keyToString(getKey(s));
			locations.set(i, s);
		}

		return locations;
	}

	// TODO remove?
	public List<Location> readByTheater(Theater theater) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		return readByTheater(KeyFactory.keyToString(theaterDAO.getKey(theater)));
	}

	public List<Location> readByName(Theater theater, String name) {
		Location l = new Location();
		l.setName(name);
		List<Location> locations = datastore.find().type(Location.class)
				.ancestor(theater)
				.addFilter("nameQuery", FilterOperator.EQUAL, l.nameQuery)
				.returnAll().now();

		return locations;
	}

	@Override
	public void update(Location location, Key key) {
		super.update(location, key);

		// update denormalized data in future performances
		// PerformanceDAO performanceDAO = performanceDAOProvider.get();
		// List<Performance> performances = performanceDAO
		// .readByLocation(KeyFactory.keyToString(key));
		// for (Performance p : performances) {
		// p.locationName = location.getName();
		// performanceDAO.update(p, performanceDAO.getKey(p));
		// }

	}

	@Override
	public void delete(Key key) {
		// allowed only if there are no future associated performances
		// PerformanceDAO performanceDAO = performanceDAOProvider.get();
		// List<Performance> performances = performanceDAO
		// .readByLocation(KeyFactory.keyToString(key));

		// if (0 < performances.size()) {
		// throw new IllegalArgumentException(
		// "Location with key "
		// + KeyFactory.keyToString(key)
		// +
		// " could not be deleted as there are future performances associated to it. Delete performances first.");
		// }
		super.delete(key);
	}

}
