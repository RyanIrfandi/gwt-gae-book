package org.gwtgaebook.template.client.landing;

import com.google.gwt.core.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;
import com.gwtplatform.mvp.client.*;

public class LandingView extends ViewWithUiHandlers<LandingUiHandlers>
		implements LandingPresenter.MyView {

	interface LandingViewUiBinder extends UiBinder<Widget, LandingView> {
	}

	private static LandingViewUiBinder uiBinder = GWT
			.create(LandingViewUiBinder.class);

	public final Widget widget;

	public LandingView() {
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void resetAndFocus() {
		// Focus the cursor on a field when the app loads
	}

}
