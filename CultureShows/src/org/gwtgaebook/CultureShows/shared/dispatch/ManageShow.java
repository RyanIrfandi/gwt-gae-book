package org.gwtgaebook.CultureShows.shared.dispatch;

import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Show;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch(isSecure = false)
public class ManageShow {
	@In(1)
	String theaterKey;

	@In(2)
	Constants.ManageActionType actionType;

	@In(3)
	Show show;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	Show showOut;
}
