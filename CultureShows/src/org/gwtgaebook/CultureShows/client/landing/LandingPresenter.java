package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.allen_sauer.gwt.log.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.*;
import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;
import com.gwtplatform.dispatch.client.*;

import org.gwtgaebook.CultureShows.client.*;
import org.gwtgaebook.CultureShows.shared.*;
import org.gwtgaebook.CultureShows.shared.dispatch.*;

public class LandingPresenter extends
		Presenter<LandingPresenter.MyView, LandingPresenter.MyProxy> implements
		LandingUiHandlers {

	@ProxyStandard
	@NameToken(NameTokens.landing)
	// TODO public interface MyProxy extends ProxyPlace<LandingPresenter>
	public interface MyProxy extends Proxy<LandingPresenter>, Place {
	}

	public interface MyView extends View, HasUiHandlers<LandingUiHandlers> {
		void resetAndFocus();
	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;

	@Inject
	public LandingPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager, DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
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

	@Override
	public void scheduleShow(Date date, String showName, String locationName) {
		Log.info("Presenter scheduling on " + date.toString() + " the show "
				+ showName + " at location " + locationName);
		dispatcher.execute(new ScheduleShowAction(Cookies
				.getCookie(Constants.theaterCookieName), showName),
				new DispatchCallback<ScheduleShowResult>() {
					@Override
					public void onSuccess(ScheduleShowResult result) {
						Cookies.setCookie(Constants.theaterCookieName, result
								.getTheaterKey());
						Log.info("Scheduled show");
					}
				});
	}
}
