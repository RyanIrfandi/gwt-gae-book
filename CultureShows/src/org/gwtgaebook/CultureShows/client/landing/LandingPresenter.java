package org.gwtgaebook.CultureShows.client.landing;

import java.util.*;

import com.allen_sauer.gwt.log.client.*;
import com.google.gwt.core.client.GWT; //import com.google.gwt.thirdparty.guava.common.base.Strings;
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

		public void addPerformances(Map<String, Performance> performanceMap);

		public void setPerformances(Map<String, Performance> performanceMap);

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

	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_RevealMainContent,
				this);
		requestPerformances();
	}

	@Override
	public void scheduleShow(Date date, String showName, String locationName) {
		Log.info("Requested performance scheduling on " + date.toString()
				+ " the show " + showName + " at location " + locationName
				+ " for theater " + UserInfoStatic.currentTheaterKey);
		if (null != UserInfoStatic.userInfo) {
			if (UserInfoStatic.userInfo.isSignedIn) {
				// make the server request
				dispatcher.execute(new ScheduleShowAction(
						UserInfoStatic.currentTheaterKey, date, showName,
						locationName),
						new DispatchCallback<ScheduleShowResult>() {
							@Override
							public void onSuccess(ScheduleShowResult result) {
								if (!result.getErrorText().isEmpty()) {
									// TODO have a general handler for this
									Window.alert(result.getErrorText());
									return;
								}
								UserInfoStatic.currentTheaterKey = result
										.getTheaterKeyOut();
								getView().addPerformances(
										result.getPerformancesMap());
							}
						});

			} else {
				// save Performance data in cookie so it is available on user
				// return
				// TODO set in proper format, depending on i18n
				Cookies.setCookie(Constants.PerformanceDateCookieName, date
						.toString());
				Cookies.setCookie(Constants.PerformanceShowNameCookieName,
						showName);
				Cookies.setCookie(Constants.PerformanceLocationNameCookieName,
						locationName);
				// ask user to sign in instead of making the server request
				requestSignIn();
			}
		}
		// TODO should handle case when (null == UserInfoStatic.userInfo),
		// meaning getUserInfo request never returned. Nothing will happen on
		// Schedule Show
	}

	@Override
	protected void onBind() {
		super.onBind();

		addRegisteredHandler(UserInfoAvailableEvent.getType(),
				new UserInfoAvailableHandler() {
					@Override
					public void onHasUserInfoAvailable(
							UserInfoAvailableEvent event) {
						onUserInfoAvailable();

					}
				});
	}

	public void requestSignIn() {
		SignInEvent.fire(this);
	}

	public void requestPerformances() {
		// Strings.isNullOrEmpty(UserInfoStatic.currentTheaterKey)
		if (!(null == UserInfoStatic.currentTheaterKey || UserInfoStatic.currentTheaterKey
				.isEmpty())) {
			dispatcher.execute(new GetPerformancesAction(
					UserInfoStatic.currentTheaterKey),
					new DispatchCallback<GetPerformancesResult>() {
						@Override
						public void onSuccess(GetPerformancesResult result) {
							if (!result.getErrorText().isEmpty()) {
								// TODO have a general handler for this
								Window.alert(result.getErrorText());
								return;
							}
							getView().setPerformances(
									result.getPerformancesMap());
						}
					});
		}

	}

	public void onUserInfoAvailable() {

		getView().setSignInOut(UserInfoStatic.userInfo);

		if (UserInfoStatic.userInfo.isSignedIn) {
			// Strings.isNullOrEmpty(Cookies.getCookie(Constants.PerformanceDateCookieName))
			if (!(null == Cookies
					.getCookie(Constants.PerformanceDateCookieName) || Cookies
					.getCookie(Constants.PerformanceDateCookieName).isEmpty())) {
				// TODO convert to date
				// Cookies.getCookie(Constants.PerformanceDateCookieName)
				scheduleShow(
						new Date(),
						Cookies
								.getCookie(Constants.PerformanceShowNameCookieName),
						Cookies
								.getCookie(Constants.PerformanceLocationNameCookieName));
				Cookies.removeCookie(Constants.PerformanceDateCookieName);
				Cookies.removeCookie(Constants.PerformanceShowNameCookieName);
				Cookies
						.removeCookie(Constants.PerformanceLocationNameCookieName);
			}

			requestPerformances();
		}
	}
}
