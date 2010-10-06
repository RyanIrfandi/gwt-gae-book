package org.gwtgaebook.CultureShows.server.dispatch;

import com.google.inject.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.server.actionhandler.*;
import com.gwtplatform.dispatch.shared.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;

public class ScheduleShowHandler extends
		DispatchActionHandler<ScheduleShowAction, ScheduleShowResult> {

	@Override
	public ScheduleShowResult execute(ScheduleShowAction action,
			ExecutionContext context) throws ActionException {

		// TODO Verify that the input is valid.
		// if (!FieldVerifier.isValidName(input)) {
		// // If the input is not valid, throw an IllegalArgumentException
		// // back to
		// // the client.
		// throw new ActionException(
		// "Name must be at least 4 characters long");
		// }
		System.out.println("Handler scheduling show " + action.getShowName());

		return new ScheduleShowResult("");
	}

}
