package org.gwtgaebook.template.client.widget;

import org.gwtgaebook.template.client.ClientState;
import org.gwtgaebook.template.client.NameTokens;
import org.gwtgaebook.template.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HeaderView extends ViewWithUiHandlers<HeaderUiHandlers> implements
		HeaderPresenter.MyView {

	interface HeaderViewUiBinder extends UiBinder<Widget, HeaderView> {
	}

	private static HeaderViewUiBinder uiBinder = GWT
			.create(HeaderViewUiBinder.class);
	private final Widget widget;

	private ClientState clientState;

	@Inject
	Resources resources;

	@UiField
	MenuBar headerMenu;
	@UiField
	MenuItem accountItem;
	@UiField
	MenuItem accountHelpItem;
	@UiField
	MenuItem accountSignOutItem;

	public HeaderView() {
		widget = uiBinder.createAndBindUi(this);

		// menu commands
		accountSignOutItem.setCommand(new Command() {
			public void execute() {
				Window.Location.replace(clientState.userInfo.signOutURL);

			}
		});

		accountHelpItem.setCommand(new Command() {
			public void execute() {
				History.newItem("");
			}
		});

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void setClientState(ClientState clientState) {
		this.clientState = clientState;
		if (null != clientState.userInfo && clientState.userInfo.isSignedIn) {
			headerMenu.setVisible(true);
			accountItem.setHTML(clientState.userInfo.email + " <img src=\""
					+ resources.menuBarDownIcon().getURL()
					+ "\" style=\"vertical-align: middle\" />");
		} else {
		}

	}
}