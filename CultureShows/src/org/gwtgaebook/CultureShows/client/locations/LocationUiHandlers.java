package org.gwtgaebook.CultureShows.client.locations;

import org.gwtgaebook.CultureShows.client.locations.model.Location;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LocationUiHandlers extends UiHandlers {
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onLocationSelected(Location l);

	public void create(String name, String websiteURL);

	public void update(String showKey, String name, String websiteURL);

	public void delete(String showKey);

}
