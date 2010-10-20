package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.allen_sauer.gwt.log.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.*;
import com.google.inject.*;
import com.gwtplatform.mvp.client.*;
import com.gwtplatform.mvp.client.annotations.*;
import com.gwtplatform.mvp.client.proxy.*;
import com.gwtplatform.dispatch.client.*;

import org.gwtgaebook.CultureShows.client.*;
import org.gwtgaebook.CultureShows.client.event.SignInEvent;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent.UserInfoAvailableHandler;
import org.gwtgaebook.CultureShows.client.util.*;
import org.gwtgaebook.CultureShows.shared.*;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.shared.model.*;

public class LandingPresenter extends
		Presenter<LandingPresenter.MyView, LandingPresenter.MyProxy> implements
		LandingUiHandlers {

	@ProxyStandard
	@NameToken(NameTokens.landing)
	public interface MyProxy extends ProxyPlace<LandingPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<LandingUiHandlers> {
		public void resetAndFocus();

		public void addPerformance(Performance performance);

		public void setPerformances(List<Performance> performances);

		public void setSignInOut(UserInfo userInfo);
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
		RevealContentEvent.fire(this, MainPresenter.TYPE_RevealMainContent,
				this);
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

	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(UserInfoAvailableEvent.getType(),
				new UserInfoAvailableHandler() {
					@Override
					public void onHasUserInfoAvailable(
							UserInfoAvailableEvent event) {
						setSignInOut(event.getUserInfo());

					}
				});
	}

	public void setSignInOut(UserInfo userInfo) {
		getView().setSignInOut(userInfo);
	}

	public void requestSignIn() {
		SignInEvent.fire(this);
	}
}