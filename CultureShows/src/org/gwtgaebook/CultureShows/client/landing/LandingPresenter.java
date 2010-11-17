package org.gwtgaebook.CultureShows.client.landing;

import java.util.Date;
import java.util.List;

import org.gwtgaebook.CultureShows.client.ClientState;
import org.gwtgaebook.CultureShows.client.DispatchCallback;
import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.MainPresenter;
import org.gwtgaebook.CultureShows.client.NameTokens;
import org.gwtgaebook.CultureShows.client.event.SignInEvent;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent.UserInfoAvailableHandler;
import org.gwtgaebook.CultureShows.shared.Constants;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetPerformancesResult;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceAction;
import org.gwtgaebook.CultureShows.shared.dispatch.ManagePerformanceResult;
import org.gwtgaebook.CultureShows.shared.model.Performance;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class LandingPresenter extends
		Presenter<LandingPresenter.MyView, LandingPresenter.MyProxy> implements
		LandingUiHandlers {

	@ProxyStandard
	@NameToken(NameTokens.landing)
	public interface MyProxy extends ProxyPlace<LandingPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<LandingUiHandlers> {
		public void resetAndFocus();

		public void setDefaultValues();

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
					.getCookie(Constants.performanceDateCookieName) || Cookies
					.getCookie(Constants.performanceDateCookieName).isEmpty())) {

				DateTimeFormat dateFormat = DateTimeFormat
						.getFormat(Constants.defaultDateFormat);

				Date date;
				date = dateFormat.parse(Cookies
						.getCookie(Constants.performanceDateCookieName));
				// TODO handle parse errors
				createPerformance(
						date,
						Cookies.getCookie(Constants.performanceShowNameCookieName),
						Cookies.getCookie(Constants.performanceLocationNameCookieName));
				Cookies.removeCookie(Constants.performanceDateCookieName);
				Cookies.removeCookie(Constants.performanceShowNameCookieName);
				// Cookies.removeCookie(Constants.PerformanceLocationNameCookieName);

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

	@Override
	public void createPerformance(Date date, String showName,
			String locationName) {
		Main.logger.info("Requested performance scheduling on "
				+ date.toString() + " the show " + showName + " at location "
				+ locationName + " for theater "
				+ clientState.currentTheaterKey);
		if (null != clientState.userInfo) {
			if (clientState.userInfo.isSignedIn) {
				// make the server request
				Performance p = new Performance();
				p.date = date;
				p.showName = showName;
				p.locationName = locationName;

				dispatcher.execute(new ManagePerformanceAction(
						clientState.currentTheaterKey,
						Constants.ManageActionType.CREATE, p),
						new DispatchCallback<ManagePerformanceResult>() {
							@Override
							public void onSuccess(ManagePerformanceResult result) {
								if (!result.getErrorText().isEmpty()) {
									// TODO have a general handler for this
									Window.alert(result.getErrorText());
									return;
								}
								// getView().addPerformance(
								// result.getPerformance());
								// remember last used location
								Cookies.setCookie(
										Constants.performanceLocationNameCookieName,
										result.getPerformanceOut().locationName);
								getView().setDefaultValues();
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
				Cookies.setCookie(Constants.performanceDateCookieName,
						dateFormat.format(date));
				Cookies.setCookie(Constants.performanceShowNameCookieName,
						showName);
				Cookies.setCookie(Constants.performanceLocationNameCookieName,
						locationName);
				// ask user to sign in instead of making the server request
				requestSignIn();
			}
		}
		// TODO should handle case when (null == clientState.userInfo),
		// meaning getUserInfo request never returned. Now nothing will happen
	}

	@Override
	public void updatePerformance(String performanceKey, Date date,
			String showName, String locationName) {
		Main.logger.info("Requested performance update for " + performanceKey
				+ " with date " + date.toString() + " the show " + showName
				+ " at location " + locationName + " for theater "
				+ clientState.currentTheaterKey);

		Performance p = new Performance();
		p.performanceKey = performanceKey;
		p.date = date;
		p.showName = showName;
		p.locationName = locationName;

		dispatcher.execute(new ManagePerformanceAction(
				clientState.currentTheaterKey,
				Constants.ManageActionType.UPDATE, p),
				new DispatchCallback<ManagePerformanceResult>() {
					@Override
					public void onSuccess(ManagePerformanceResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshPerformances();
					}
				});

	}

	@Override
	public void deletePerformance(String performanceKey) {
		Main.logger.info("Requested performance update for " + performanceKey);

		Performance p = new Performance();
		p.performanceKey = performanceKey;

		dispatcher.execute(new ManagePerformanceAction(
				clientState.currentTheaterKey,
				Constants.ManageActionType.DELETE, p),
				new DispatchCallback<ManagePerformanceResult>() {
					@Override
					public void onSuccess(ManagePerformanceResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						getView().setDefaultValues();
						getView().refreshPerformances();
					}
				});

	}

}
