package org.gwtgaebook.CultureShows.shared.dispatch;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class ScheduleShow {
	@In(1)
	String userToken;

	@In(2)
	String theaterKey;

	@In(3)
	String showName;

	@In(4)
	String locationName;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	String theaterKeyOut;

}
