package org.gwtgaebook.CultureShows.client.landing;

import java.util.Date;

import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LandingUiHandlers extends UiHandlers {
	public void requestSignIn();

	void scheduleShow(Date date, String showName, String locationName);

	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onPerformanceSelected(Performance p);
}
