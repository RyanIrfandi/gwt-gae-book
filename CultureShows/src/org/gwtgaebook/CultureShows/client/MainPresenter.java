package org.gwtgaebook.CultureShows.client;

import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent;
import org.gwtgaebook.CultureShows.client.widget.HeaderPresenter;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> implements
		MainUiHandlers {

	@ProxyStandard
	public interface MyProxy extends Proxy<MainPresenter> {
	}

	public interface MyView extends View, HasUiHandlers<MainUiHandlers> {
	}

	public static final Object TYPE_RevealHeaderContent = new Object();
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_RevealPageContent = new Type<RevealContentHandler<?>>();

	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;
	private ClientState clientState;

	private final HeaderPresenter headerPresenter;

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final PlaceManager placeManager,
			final DispatchAsync dispatcher,
			final HeaderPresenter headerPresenter, final ClientState clientState) {
		super(eventBus, view, proxy);
		this.placeManager = placeManager;
		this.dispatcher = dispatcher;
		this.headerPresenter = headerPresenter;
		this.clientState = clientState;

		getView().setUiHandlers(this);

	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	public void onReveal() {
		super.onReveal();
		Main.logger.info("MainPresenter onReveal");

		setInSlot(TYPE_RevealHeaderContent, headerPresenter);
	}

	@Override
	protected void onBind() {
		super.onBind();

		// dispatcher.execute(new GetUserAction(Window.Location.getHref()),
		// new DispatchCallback<GetUserResult>() {
		// @Override
		// public void onSuccess(GetUserResult result) {
		// if (!result.getErrorText().isEmpty()) {
		// // TODO have a general handler for this
		// Window.alert(result.getErrorText());
		// return;
		// }
		// clientState.userInfo = result.getUserInfo();
		// clientState.theaters = result.getTheaters();
		//
		// if (clientState.theaters.size() > 0) {
		// clientState.currentTheaterKey = clientState.theaters
		// .get(0).theaterKey;
		// }
		// // TODO testability broken if relying to global
		// // ClientState
		// onGetUserSuccess();
		// }
		// });
		//
		// addRegisteredHandler(SignInEvent.getType(), new SignInHandler() {
		// @Override
		// public void onHasSignIn(SignInEvent event) {
		// showSignInDialog();
		//
		// }
		// });

	}

	public void onGetUserSuccess() {
		// this.userInfo = userInfo;
		Main.logger.info("User isSignedIn "
				+ clientState.userInfo.isSignedIn.toString() + " with email "
				+ clientState.userInfo.email + " username "
				+ clientState.userInfo.userId);
		Main.logger.info("Sign In URLs "
				+ clientState.userInfo.signInURLs.toString() + " Sign Out URL "
				+ clientState.userInfo.signOutURL);
		// , UserInfouserInfo
		UserInfoAvailableEvent.fire(this, clientState);
	}

	// public void showSignInDialog() {
	// signInPresenter.setUserInfo(clientState.userInfo);
	// RevealRootPopupContentEvent.fire(this, signInPresenter);
	// }

}
