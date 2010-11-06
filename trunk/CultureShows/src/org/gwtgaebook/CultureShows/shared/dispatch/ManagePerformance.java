package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.Date;

import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.model.Performance;

import com.gwtplatform.annotation.GenDispatch;
import com.gwtplatform.annotation.In;
import com.gwtplatform.annotation.Out;

@GenDispatch(isSecure = false)
public class ManagePerformance {
	@In(1)
	Constants.ManageActionType actionType;

	@In(2)
	Performance performance;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	Performance performanceOut;
}
