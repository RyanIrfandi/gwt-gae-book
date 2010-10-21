package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.*;

import com.gwtplatform.annotation.*;

import org.gwtgaebook.CultureShows.shared.model.*;

@GenDispatch(isSecure = false)
public class GetPerformances {
	@In(1)
	String theaterKey;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	Map<String, Performance> performancesMap = new HashMap<String, Performance>();

}
