package org.gwtgaebook.CultureShows.client.locations;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

//TODO security
@GenDispatch(isSecure = false)
public class ReadLocations {
	@In(1)
	String theaterKey;

	@Out(1)
	LocationModel.Locations locations;
	// List<Location> locations = new ArrayList<Location>();

}
