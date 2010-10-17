package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.*;

import org.gwtgaebook.CultureShows.shared.model.*;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class ScheduleShow {
	@In(1)
	String userToken;

	@In(2)
	String theaterKey;

	@In(4)
	Date date;

	@In(5)
	String showName;

	@In(6)
	String locationName;

	@Out(1)
	String errorText; // empty if success

	// TODO mark as optional
	@Out(2)
	String theaterKeyOut;

	@Out(3)
	String performanceKey;

	@Out(4)
	Performance performance;

}
