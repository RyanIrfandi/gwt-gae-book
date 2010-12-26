package org.gwtgaebook.CultureShows.client.shows.dispatch;

import org.gwtgaebook.CultureShows.client.shows.model.Shows;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

//TODO security
@GenDispatch(isSecure = false)
public class ReadShows {
	@In(1)
	String theaterKey;

	@Out(1)
	Shows shows;
}
