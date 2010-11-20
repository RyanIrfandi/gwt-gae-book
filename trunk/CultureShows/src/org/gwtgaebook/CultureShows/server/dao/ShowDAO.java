package org.gwtgaebook.CultureShows.server.dao;

import java.util.List;

import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class ShowDAO extends DAO<Show> {
	@Inject
	Provider<TheaterDAO> theaterDAOProvider;

	@Inject
	Provider<PerformanceDAO> performanceDAOProvider;

	@Inject
	public ShowDAO(final ObjectDatastore datastore) {
		super(Show.class, datastore);
	}

	@Override
	public Key create(Show show) {
		throw new IllegalArgumentException("Show " + show.getName()
				+ " is not associated to a theater");
	}

	public Key create(Theater theater, Show show) {
		if (null == theater) {
			throw new IllegalArgumentException("Theater for show "
					+ show.getName() + " is invalid");
		}
		return datastore.store().instance(show).parent(theater).now();
	}

	public List<Show> readByTheater(String theaterKey) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		Theater theater = theaterDAO.read(theaterKey);

		List<Show> shows = datastore.find().type(Show.class).ancestor(theater)
				.returnAll().now();

		// set key
		Show s;
		for (int i = 0; i < shows.size(); i++) {
			s = shows.get(i);
			s.showKey = KeyFactory.keyToString(getKey(s));
			shows.set(i, s);
		}

		return shows;
	}

	public List<Show> readByTheater(Theater theater) {
		List<Show> shows = datastore.find().type(Show.class).ancestor(theater)
				.returnAll().now();

		return shows;
	}

	public List<Show> readByName(Theater theater, String name) {
		List<Show> shows = datastore.find().type(Show.class).ancestor(theater)
				.addFilter("nameQuery", FilterOperator.EQUAL, name).returnAll()
				.now();

		return shows;
	}

	@Override
	public void update(Show show, Key key) {
		super.update(show, key);

		// update denormalized data in future performances
		PerformanceDAO performanceDAO = performanceDAOProvider.get();
		List<Performance> performances = performanceDAO.readByShow(KeyFactory
				.keyToString(key));
		for (Performance p : performances) {
			p.showName = show.getName();
			performanceDAO.update(p, performanceDAO.getKey(p));
		}
	}

	public void delete(Key key) {
		// allowed only if there are no future associated performances
		PerformanceDAO performanceDAO = performanceDAOProvider.get();
		List<Performance> performances = performanceDAO.readByShow(KeyFactory
				.keyToString(key));

		if (0 < performances.size()) {
			throw new IllegalArgumentException(
					"Show with key "
							+ KeyFactory.keyToString(key)
							+ " could not be deleted as there are future performances associated to it. Delete performances first.");
		}
		super.delete(key);
	}

}
