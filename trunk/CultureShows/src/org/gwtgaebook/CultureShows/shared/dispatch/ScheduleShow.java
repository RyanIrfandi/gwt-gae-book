package org.gwtgaebook.CultureShows.shared.dispatch;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class ScheduleShow {
	@In(1)
	String showName;

	@Out(1)
	String response;
}
