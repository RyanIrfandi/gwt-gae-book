package org.gwtgaebook.CultureShows.client.landing;

import java.util.Date;

import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.gwtplatform.mvp.client.UiHandlers;

public interface LandingUiHandlers extends UiHandlers {
	public void requestSignIn();

	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onPerformanceSelected(Performance p);

	public void createPerformance(Date date, String showName,
			String locationName);

	public void updatePerformance(String performanceKey, Date date,
			String showName, String locationName);

	public void deletePerformance(String performanceKey);

}
