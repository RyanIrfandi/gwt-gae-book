package org.gwtgaebook.template.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.*;
import com.gwtplatform.mvp.client.*;

public class Main implements EntryPoint {

	public final MainGinjector ginjector = GWT.create(MainGinjector.class);
	public static Logger logger = Logger.getLogger("");

	public void onModuleLoad() {

		DelayedBindRegistry.bind(ginjector);
		ginjector.getResources().style().ensureInjected();
		ginjector.getPlaceManager().revealCurrentPlace();

	}

}
