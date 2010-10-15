package org.gwtgaebook.CultureShows.shared.model;

import java.util.*;

import com.google.appengine.api.datastore.*;
import com.google.code.twig.annotation.Index;

public class Performance {
	@Index
	public Key showKey;
	@Index
	public Key locationKey;
	@Index
	public Date date;

	// denormalized
	@Index
	public Key theaterKey;
	// TODO can performance be queried for theaters without theaterKey, but with
	// showKey which contains it?

	public String showName;
	public String showWebsiteURL;
	public String locationName;
}
