package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.*;

import org.gwtgaebook.CultureShows.shared.model.*;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class ScheduleShow {
	@In(1)
	String theaterKey;

	@In(2)
	Date date;

	@In(3)
	String showName;

	@In(4)
	String locationName;

	@Out(1)
	String errorText; // empty if success

	// TODO mark as optional
	@Out(2)
	String theaterKeyOut;

	@Out(3)
	Map<String, Performance> performancesMap = new HashMap<String, Performance>();

}
