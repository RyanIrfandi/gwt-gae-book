package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.allen_sauer.gwt.log.client.*;
import com.google.gwt.user.client.*;
import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;
import com.gwtplatform.dispatch.client.*;

import org.gwtgaebook.CultureShows.client.*;
import org.gwtgaebook.CultureShows.client.util.*;
import org.gwtgaebook.CultureShows.shared.*;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

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

		public void addPerformance(Performance performance);

		public void setPerformances(List<Performance> performances);
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

		// if not signed in, set a random user token to emulate signed in
		// functionality
		// TODO set only if not signed in
		if (null == Cookies.getCookie(Constants.userTokenCookieName)
				|| Cookies.getCookie(Constants.userTokenCookieName).isEmpty()) {
			Cookies.setCookie(Constants.userTokenCookieName, MyUUID.uuid());
		}

	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().resetAndFocus();

		if (!(null == Cookies.getCookie(Constants.theaterCookieName) || Cookies
				.getCookie(Constants.theaterCookieName).isEmpty())) {
			dispatcher.execute(new GetPerformancesAction(Cookies
					.getCookie(Constants.theaterCookieName)),
					new DispatchCallback<GetPerformancesResult>() {
						@Override
						public void onSuccess(GetPerformancesResult result) {
							if (!result.getErrorText().isEmpty()) {
								// TODO have a general handler for this
								Window.alert(result.getErrorText());
								return;
							}
							getView().setPerformances(result.getPerformances());
						}
					});
		}

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
				.getCookie(Constants.userTokenCookieName), Cookies
				.getCookie(Constants.theaterCookieName), date, showName,
				locationName), new DispatchCallback<ScheduleShowResult>() {
			@Override
			public void onSuccess(ScheduleShowResult result) {
				if (!result.getErrorText().isEmpty()) {
					// TODO have a general handler for this
					Window.alert(result.getErrorText());
					return;
				}
				Cookies.setCookie(Constants.theaterCookieName, result
						.getTheaterKeyOut());
				getView().addPerformance(result.getPerformance());
			}
		});
	}
}
