package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.ArrayList;
import java.util.List;

import org.gwtgaebook.CultureShows.shared.model.Show;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch(isSecure = false)
public class GetShows {
	@In(1)
	String theaterKey;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	List<Show> shows = new ArrayList<Show>();
}
