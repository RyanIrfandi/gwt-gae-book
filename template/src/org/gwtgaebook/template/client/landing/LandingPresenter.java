package org.gwtgaebook.template.client.landing;

import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;

import org.gwtgaebook.template.client.*;

public class LandingPresenter extends
		Presenter<LandingPresenter.MyView, LandingPresenter.MyProxy> implements
		LandingUiHandlers {

	@ProxyStandard
	@NameToken(NameTokens.landing)
	public interface MyProxy extends Proxy<LandingPresenter>, Place {
	}

	public interface MyView extends View, HasUiHandlers<LandingUiHandlers> {
		void resetAndFocus();

	}

	private final PlaceManager placeManager;

	@Inject
	public LandingPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		getView().setUiHandlers(this);

	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().resetAndFocus();
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

}
