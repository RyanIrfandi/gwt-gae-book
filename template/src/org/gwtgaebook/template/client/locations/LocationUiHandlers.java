package org.gwtgaebook.template.client.locations;

import org.gwtgaebook.template.client.locations.model.Location;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LocationUiHandlers extends UiHandlers {
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onLocationSelected(Location l);

	public void create(Location l);

	public void update(Location l);

	public void delete(String locationKey);

}
