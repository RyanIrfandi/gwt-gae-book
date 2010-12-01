package org.gwtgaebook.CultureShows.server.dispatch;

import org.gwtgaebook.CultureShows.shared.dispatch.GetUserSampleAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserSampleResult;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class GetUserSampleHandler implements
		ActionHandler<GetUserSampleAction, GetUserSampleResult> {

	@Override
	public GetUserSampleResult execute(GetUserSampleAction action,
			ExecutionContext context) throws ActionException {

		System.out.println("Client URI: " + action.getRequestURI());

		return new GetUserSampleResult("not signed in");
	}

	@Override
	public Class<GetUserSampleAction> getActionType() {
		return GetUserSampleAction.class;
	}

	@Override
	public void undo(GetUserSampleAction action, GetUserSampleResult result,
			ExecutionContext context) throws ActionException {
		// Not undoable
	}

}