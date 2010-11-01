package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.ArrayList;
import java.util.List;

import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch(isSecure = false)
public class GetPerformances {
	@In(1)
	String theaterKey;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	List<Performance> performances = new ArrayList<Performance>();
}
