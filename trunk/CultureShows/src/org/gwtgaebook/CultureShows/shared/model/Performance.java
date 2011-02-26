package org.gwtgaebook.CultureShows.shared.model;

import java.io.Serializable;
import java.util.Date;

import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Store;
import com.google.gson.annotations.Expose;
import com.google.gwt.view.client.ProvidesKey;

// needs to be Serializable to be sent over GWT-RPC as part of action/result
@SuppressWarnings("serial")
public class Performance implements Serializable {
	public static final ProvidesKey<Performance> KEY_PROVIDER = new ProvidesKey<Performance>() {
		public Object getKey(Performance p) {
			return (null == p) ? null : p.performanceKey;
		}
	};

	@Store(false)
	@Expose
	public String performanceKey;

	@Index
	@Expose
	public String date;

	@Expose
	public String timeHourMinute;

	@Index
	public String showKey;

	@Index
	public String locationKey;

	// denormalized
	public String showName;
	public String showWebsiteURL;
	public String locationName;

	@Store(false)
	@Expose
	public transient Show show;

	@Store(false)
	@Expose
	public transient Location location;

}
