package org.gwtgaebook.CultureShows.client.landing;

import org.gwtgaebook.CultureShows.client.ClientState;
import org.gwtgaebook.CultureShows.client.DispatchCallback;
import org.gwtgaebook.CultureShows.client.Main;
import org.gwtgaebook.CultureShows.client.MainPresenter;
import org.gwtgaebook.CultureShows.client.NameTokens;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserResult;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserSampleAction;
import org.gwtgaebook.CultureShows.shared.dispatch.GetUserSampleResult;
import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
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
		public void setUserInfo(UserInfo userInfo);

	}

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	@Inject
	public LandingPresenter(EventBus eventBus, MyView view, MyProxy proxy,
			final PlaceManager placeManager, DispatchAsync dispatcher,
			final ClientState clientState) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);

		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.clientState = clientState;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_RevealPageContent,
				this);
		Main.logger.info("Revealing Landing");

	}

	@Override
	protected void onBind() {
		super.onBind();

		dispatcher.execute(new GetUserAction(Window.Location.getHref()),
				new DispatchCallback<GetUserResult>() {
					@Override
					public void onSuccess(GetUserResult result) {
						if (!result.getErrorText().isEmpty()) {
							// TODO have a general handler for this
							Window.alert(result.getErrorText());
							return;
						}
						clientState.userInfo = result.getUserInfo();
						clientState.theaters = result.getTheaters();

						if (clientState.theaters.size() > 0) {
							clientState.currentTheaterKey = clientState.theaters
									.get(0).theaterKey;
						}
						// TODO testability broken if relying to global
						// ClientState
						onGetUserSuccess();

					}
				});

		// dispatcher.execute(new
		// GetUserSampleAction(Window.Location.getHref()),
		// new AsyncCallback<GetUserSampleResult>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// Main.logger.severe("GetUserSample call failed "
		// + caught.getMessage());
		// }
		//
		// @Override
		// public void onSuccess(GetUserSampleResult result) {
		// Main.logger.info("GetUserSample result: "
		// + result.getResponse());
		// }
		// });

		// dispatcher.execute(new
		// GetUserSampleAction(Window.Location.getHref()),
		// new DispatchCallback<GetUserSampleResult>() {
		//
		// @Override
		// public void onSuccess(GetUserSampleResult result) {
		// Main.logger.info("GetUserSample result: "
		// + result.getResponse());
		// }
		// });

	}

	public void onGetUserSuccess() {
		Main.logger.info("onGetUserSuccess: User isSignedIn "
				+ clientState.userInfo.isSignedIn.toString() + " with email "
				+ clientState.userInfo.email + " username "
				+ clientState.userInfo.userId);

		UserInfoAvailableEvent.fire(this, clientState);

		if (clientState.userInfo.isSignedIn) {
			PlaceRequest myRequest = new PlaceRequest(NameTokens.performances);
			placeManager.revealPlace(myRequest);
		} else {
			getView().setUserInfo(clientState.userInfo);
		}

	}

}
