package org.gwtgaebook.CultureShows.shared.model;

import java.io.Serializable;

import com.google.code.twig.annotation.*;
import com.google.gson.annotations.Expose;
import com.google.gwt.view.client.ProvidesKey;

@SuppressWarnings("serial")
public class Show implements Serializable {
	public static final ProvidesKey<Show> KEY_PROVIDER = new ProvidesKey<Show>() {
		public Object getKey(Show s) {
			return (null == s) ? null : s.showKey;
		}
	};

	@Store(false)
	@Expose
	public String showKey;

	@Expose
	private String name;

	@Index
	public String nameQuery;
	// used in queries. It's always trim and lowercase

	@Expose
	public String websiteURL;

	@Expose
	public int minuteDuration;

	@Store(false)
	@Expose
	public String duration; // hh:mm

	@Expose
	public String posterURL;

	public void setName(String name) {
		this.name = name;
		nameQuery = name.trim().toLowerCase();
	}

	public String getName() {
		return name;

	}

}