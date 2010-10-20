package org.gwtgaebook.CultureShows.client.widget;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.inject.Inject;

import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;

public class SignInPresenter extends PresenterWidget<SignInPresenter.MyView> {

	public interface MyView extends PopupView {
		public void setUserInfo(UserInfo userInfo);
	}

	@Inject
	public SignInPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);

	}

	public void setUserInfo(final UserInfo userInfo) {
		getView().setUserInfo(userInfo);

	}

}