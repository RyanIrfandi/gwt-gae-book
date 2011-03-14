package org.gwtgaebook.CultureShows.server.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Location;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.Show;
import org.gwtgaebook.CultureShows.shared.model.Theater;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class PerformanceDAO extends DAO<Performance> {
	@Inject
	Provider<TheaterDAO> theaterDAOProvider;
	@Inject
	Provider<ShowDAO> showDAOProvider;
	@Inject
	Provider<LocationDAO> locationDAOProvider;

	@Inject
	public PerformanceDAO(final ObjectDatastore datastore) {
		super(Performance.class, datastore);
	}

	@Override
	public Key create(Performance performance) {
		throw new IllegalArgumentException("Performance on date "
				+ performance.date.toString()
				+ " is not associated to a theater");
	}

	private Performance setDenormalizedData(Performance performance) {
		// handle denormalized fields
		ShowDAO showDAO = showDAOProvider.get();
		Show show = showDAO.read(performance.showKey);
		performance.showName = show.getName();
		performance.showWebsiteURL = show.websiteURL;

		LocationDAO locationDAO = locationDAOProvider.get();
		Location location = locationDAO.read(performance.locationKey);
		performance.locationName = location.getName();

		return performance;
	}

	public Key create(Theater theater, Performance performance) {
		if (null == theater) {
			throw new IllegalArgumentException(
					"Theater for performance on date "
							+ performance.date.toString() + " is invalid");
		}

		performance = setDenormalizedData(performance);

		return datastore.store().instance(performance).parent(theater).now();
	}

	public List<Performance> readByTheater(String theaterKey) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		Theater theater = theaterDAO.read(theaterKey);

		// by default, get only future performances
		Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat(Constants.serverDateFormat);

		List<Performance> performances = datastore.find()
				.type(Performance.class).ancestor(theater)
				.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, df.format(date))
				.addSort("date").returnAll().now();

		// add key to model, so it can be sent to client
		Performance p;
		for (int i = 0; i < performances.size(); i++) {
			p = performances.get(i);
			p.performanceKey = KeyFactory.keyToString(getKey(p));
			performances.set(i, p);
		}

		return performances;
	}

	public List<Performance> readByTheaterIncludingPast(String theaterKey) {
		TheaterDAO theaterDAO = theaterDAOProvider.get();
		Theater theater = theaterDAO.read(theaterKey);

		List<Performance> performances = datastore.find()
				.type(Performance.class).ancestor(theater)
				.addFilter("theaterKey", FilterOperator.EQUAL, theaterKey)
				.addSort("date").returnAll().now();

		return performances;
	}

	public List<Performance> readByShow(String showKey) {
		// by default, get only future performances
		Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat(Constants.serverDateFormat);
		List<Performance> performances = datastore.find()
				.type(Performance.class)
				.addFilter("showKey", FilterOperator.EQUAL, showKey)
				.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, df.format(date))
				.addSort("date").returnAll().now();

		return performances;
	}

	public List<Performance> readByLocation(String locationKey) {
		// by default, get only future performances
		Date date = new java.util.Date();
		DateFormat df = new SimpleDateFormat(Constants.serverDateFormat);
		List<Performance> performances = datastore.find()
				.type(Performance.class)
				.addFilter("locationKey", FilterOperator.EQUAL, locationKey)
				.addFilter("date", FilterOperator.GREATER_THAN_OR_EQUAL, df.format(date))
				.addSort("date").returnAll().now();

		return performances;
	}

	@Override
	public void update(Performance performance, Key key) {
		performance = setDenormalizedData(performance);

		super.update(performance, key);

	}

}
