package org.gwtgaebook.CultureShows.server.dispatch;

import java.lang.reflect.*;
import java.util.logging.*;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.code.twig.*;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.*;
import com.gwtplatform.dispatch.server.actionhandler.*;
import com.gwtplatform.dispatch.shared.*;

public abstract class DispatchActionHandler<A extends Action<R>, R extends Result>
		implements ActionHandler<A, R> {

	private final Class<A> actionClass;

	protected final Provider<UserInfo> userInfoProvider;
	protected final ObjectDatastore datastore;

	protected static final Logger logger = Logger
			.getLogger(DispatchActionHandler.class.getName());

	@SuppressWarnings("unchecked")
	public DispatchActionHandler(final Provider<UserInfo> userInfoProvider,
			final ObjectDatastore datastore) {
		// this works because DispatchActionHandler is abstract. See
		// http://www.bewareofthebear.com/java/instantiating-generic-type-parameters/
		this.actionClass = (Class<A>) ((ParameterizedType) this.getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.userInfoProvider = userInfoProvider;
		this.datastore = datastore;

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
