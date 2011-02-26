package org.gwtgaebook.CultureShows.client.performances;

import java.util.Date;

import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.gwtplatform.mvp.client.UiHandlers;

public interface PerformanceUiHandlers extends UiHandlers {

	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onPerformanceSelected(Performance p);

	public void createPerformance(String date, String timeHourMinute,
			String showName, String locationName);

	public void updatePerformance(String performanceKey, String date,
			String timeHourMinute, String showName, String locationName);

	public void deletePerformance(String performanceKey);

}
