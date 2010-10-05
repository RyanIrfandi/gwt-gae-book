package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.gwtplatform.mvp.client.*;

public interface LandingUiHandlers extends UiHandlers {
	void scheduleShow(Date date, String showName, String locationName);
}
