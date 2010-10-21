package org.gwtgaebook.CultureShows.client.event;

import org.gwtgaebook.CultureShows.shared.model.UserInfo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class UserInfoAvailableEvent extends
		GwtEvent<UserInfoAvailableEvent.UserInfoAvailableHandler> {

	public interface HasUserInfoAvailableHandlers extends HasHandlers {
		HandlerRegistration addUserInfoAvailableHandler(
				UserInfoAvailableHandler handler);
	}

	public interface UserInfoAvailableHandler extends EventHandler {
		void onHasUserInfoAvailable(UserInfoAvailableEvent event);
	}

	private static Type<UserInfoAvailableHandler> TYPE = new Type<UserInfoAvailableHandler>();

	public static Type<UserInfoAvailableHandler> getType() {
		return TYPE;
	}

	@Override
	public Type<UserInfoAvailableHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(UserInfoAvailableHandler handler) {
		handler.onHasUserInfoAvailable(this);
	}

	public static void fire(HasHandlers source, UserInfo userInfo) {
		if (TYPE != null) {
			source.fireEvent(new UserInfoAvailableEvent(userInfo));
		}
	}

	private UserInfo userInfo;

	public UserInfoAvailableEvent(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public UserInfo getUserInfo() {
		return this.userInfo;
	}

}