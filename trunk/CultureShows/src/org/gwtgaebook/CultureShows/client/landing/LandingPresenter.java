package org.gwtgaebook.CultureShows.client.landing;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT; //import com.google.gwt.thirdparty.guava.common.base.Strings;
import com.google.gwt.i18n.client.DateTimeFormat;
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

		// public void addPerformance(Performance p);
		//
		// public void setPerformances(List<Performance> performances);

		public void setSignInOut(UserInfo userInfo);

		public void loadPerformanceData(Integer start, Integer length,
				List<Performance> performances);

		public void refreshPerformances();

	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	@Inject
	public LandingPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			PlaceManager placeManager, DispatchAsync dispatcher,
			final ClientState clientState) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.clientState = clientState;

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
		Main.logger.info("Requested performance scheduling on "
				+ date.toString() + " the show " + showName + " at location "
				+ locationName + " for theater "
				+ clientState.currentTheaterKey);
		if (null != clientState.userInfo) {
			if (clientState.userInfo.isSignedIn) {
				// make the server request
				dispatcher.execute(new ScheduleShowAction(
						clientState.currentTheaterKey, date, showName,
						locationName),
						new DispatchCallback<ScheduleShowResult>() {
							@Override
							public void onSuccess(ScheduleShowResult result) {
								if (!result.getErrorText().isEmpty()) {
									// TODO have a general handler for this
									Window.alert(result.getErrorText());
									return;
								}
								clientState.currentTheaterKey = result
										.getTheaterKeyOut();
								// getView().addPerformance(
								// result.getPerformance());
								getView().refreshPerformances();
							}
						});

			} else {
				// save Performance data in cookie so it is available on user
				// return
				// this date format shouldn't be i18n'ed, it's only for internal
				// use
				DateTimeFormat dateFormat = DateTimeFormat
						.getFormat(Constants.defaultDateFormat);
				Cookies.setCookie(Constants.PerformanceDateCookieName,
						dateFormat.format(date));
				Cookies.setCookie(Constants.PerformanceShowNameCookieName,
						showName);
				Cookies.setCookie(Constants.PerformanceLocationNameCookieName,
						locationName);
				// ask user to sign in instead of making the server request
				requestSignIn();
			}
		}
		// TODO should handle case when (null == clientState.userInfo),
		// meaning getUserInfo request never returned. Now nothing will happen
		// on
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
						onUserInfoAvailable(event.getUserInfo());

					}
				});
	}

	public void requestSignIn() {
		SignInEvent.fire(this);
	}

	public void requestPerformances() {
		// Strings.isNullOrEmpty(clientState.currentTheaterKey)
		if (!(null == clientState.currentTheaterKey || clientState.currentTheaterKey
				.isEmpty())) {
			dispatcher.execute(new GetPerformancesAction(
					clientState.currentTheaterKey),
					new DispatchCallback<GetPerformancesResult>() {
						@Override
						public void onSuccess(GetPerformancesResult result) {
							if (!result.getErrorText().isEmpty()) {
								// TODO have a general handler for this
								Window.alert(result.getErrorText());
								return;
							}
							// getView().setPerformances(result.getPerformances());
							// TODO result.getPageStart()
							getView().loadPerformanceData(
									Constants.visibleRangeStart,
									result.getPerformances().size(),
									result.getPerformances());
						}
					});
		}

	}

	public void onUserInfoAvailable(UserInfo userInfo) {
		clientState.userInfo = userInfo;
		getView().setSignInOut(clientState.userInfo);

		if (clientState.userInfo.isSignedIn) {
			// Strings.isNullOrEmpty(Cookies.getCookie(Constants.PerformanceDateCookieName))
			if (!(null == Cookies
					.getCookie(Constants.PerformanceDateCookieName) || Cookies
					.getCookie(Constants.PerformanceDateCookieName).isEmpty())) {

				DateTimeFormat dateFormat = DateTimeFormat
						.getFormat(Constants.defaultDateFormat);

				Date date;
				date = dateFormat.parse(Cookies
						.getCookie(Constants.PerformanceDateCookieName));
				// won't bother with parse errors; worst case anonymous
				// scheduled performance will be messed
				scheduleShow(
						date,
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

	@Override
	public void onRangeOrSizeChanged(Integer visibleRangeStart,
			Integer visibleRangeLength) {
		// usually, this should have requested a new set of data from server for
		// visible range. Not needed on upcoming performances, just fetch them
		// all
		requestPerformances();
	}

	public void onPerformanceSelected(Performance p) {
		Main.logger.info("Selected performance " + p.performanceKey
				+ " with show " + p.showName);
	}
}
