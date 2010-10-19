package org.gwtgaebook.CultureShows.client.widget;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;

import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent;
import org.gwtgaebook.CultureShows.client.event.UserInfoAvailableEvent.UserInfoAvailableHandler;

public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> {

	public interface MyView extends View {
		public void setSignInOut(UserInfo userInfo);
	}

	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
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

}