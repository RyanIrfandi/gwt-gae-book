package org.gwtgaebook.template.client;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.proxy.*;

public class MainPlaceManager extends PlaceManagerImpl {

	@Inject
	public MainPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter) {
		super(eventBus, tokenFormatter);
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(new PlaceRequest(NameTokens.landing));
	}

}
