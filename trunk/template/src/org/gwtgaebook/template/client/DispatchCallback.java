package org.gwtgaebook.template.client;

import com.google.gwt.user.client.*;
import com.google.gwt.user.client.rpc.*;

public abstract class DispatchCallback<T> implements AsyncCallback<T> {

	public DispatchCallback() {

	}

	/**
	 * Should be overriden by clients who want to handle error cases themselves.
	 */
	@Override
	public void onFailure(Throwable caught) {
		caught.printStackTrace();
		Window.alert("RPC failed: " + caught.toString());
	}

}
