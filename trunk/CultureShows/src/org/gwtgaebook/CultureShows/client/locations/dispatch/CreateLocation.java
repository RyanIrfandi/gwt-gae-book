package org.gwtgaebook.CultureShows.client.locations.dispatch;

import org.gwtgaebook.CultureShows.client.locations.model.Location;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

//TODO security
@GenDispatch(isSecure = false)
public class CreateLocation {
	@In(1)
	String theaterKey;

	@In(2)
	Location locationIn;

	@Out(1)
	Location locationOut;
}
