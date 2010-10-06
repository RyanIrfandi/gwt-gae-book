package org.gwtgaebook.CultureShows.server.dispatch;

import java.lang.reflect.*;

import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.server.actionhandler.*;
import com.gwtplatform.dispatch.shared.*;

public abstract class DispatchActionHandler<A extends Action<R>, R extends Result>
		implements ActionHandler<A, R> {

	private final Class<A> actionClass;

	@SuppressWarnings("unchecked")
	public DispatchActionHandler() {
		// this works because DispatchActionHandler is abstract. See
		// http://www.bewareofthebear.com/java/instantiating-generic-type-parameters/
		this.actionClass = (Class<A>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@Override
	public Class<A> getActionType() {
		return actionClass;
	}

	@Override
	public void undo(A action, R result, ExecutionContext context)
			throws ActionException {
		// by default not undoable
	}

}
