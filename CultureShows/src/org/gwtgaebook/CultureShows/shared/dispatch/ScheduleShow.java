package org.gwtgaebook.CultureShows.shared.dispatch;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class ScheduleShow {
	@In(1)
	String showName;
	@In(2)
	String theaterKey;

	@Out(1)
	String response;
}
