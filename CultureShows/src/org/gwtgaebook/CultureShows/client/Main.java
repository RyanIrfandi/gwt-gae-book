package org.gwtgaebook.CultureShows.client;

import com.google.gwt.core.client.*;
import com.gwtplatform.mvp.client.*;

public class Main implements EntryPoint {

	public final MainGinjector ginjector = GWT.create(MainGinjector.class);

	public void onModuleLoad() {

		DelayedBindRegistry.bind(ginjector);
		ginjector.getResources().style().ensureInjected();
		ginjector.getPlaceManager().revealCurrentPlace();

	}

}
