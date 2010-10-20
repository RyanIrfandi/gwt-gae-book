package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.gwtplatform.mvp.client.*;

public interface LandingUiHandlers extends UiHandlers {
	public void requestSignIn();

	void scheduleShow(Date date, String showName, String locationName);
}
