package org.gwtgaebook.CultureShows.client.shows;

import org.gwtgaebook.CultureShows.shared.model.Show;

import com.gwtplatform.mvp.client.UiHandlers;

public interface ShowUiHandlers extends UiHandlers {
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength);

	void onShowSelected(Show s);

	public void create(String name, String websiteURL, int minuteDuration,
			String posterURL);

	public void update(String showKey, String name, String websiteURL,
			int minuteDuration, String posterURL);

	public void delete(String showKey);

}
