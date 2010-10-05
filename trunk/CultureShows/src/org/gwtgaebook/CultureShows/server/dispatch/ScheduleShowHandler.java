package org.gwtgaebook.CultureShows.server.dispatch;

import com.google.inject.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.server.actionhandler.*;
import com.gwtplatform.dispatch.shared.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.gwtgaebook.CultureShows.shared.dispatch.*;

public class ScheduleShowHandler implements
		ActionHandler<ScheduleShowAction, ScheduleShowResult> {

	private Provider<HttpServletRequest> requestProvider;
	private ServletContext servletContext;

	@Inject
	ScheduleShowHandler(ServletContext servletContext,
			Provider<HttpServletRequest> requestProvider) {
		this.servletContext = servletContext;
		this.requestProvider = requestProvider;
	}

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

	@Override
	public Class<ScheduleShowAction> getActionType() {
		return ScheduleShowAction.class;
	}

	@Override
	public void undo(ScheduleShowAction action, ScheduleShowResult result,
			ExecutionContext context) throws ActionException {
		// Not undoable
	}

}
