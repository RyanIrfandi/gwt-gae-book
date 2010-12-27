package org.gwtgaebook.CultureShows.client.locations.dispatch;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;

//TODO security
@GenDispatch(isSecure = false)
public class DeleteLocation {
	@In(1)
	String theaterKey;

	@In(2)
	String locationKey;
}
