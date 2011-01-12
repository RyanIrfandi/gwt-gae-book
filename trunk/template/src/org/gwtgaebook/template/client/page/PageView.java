package org.gwtgaebook.template.client.page;

import org.gwtgaebook.template.client.ClientState;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class PageView extends ViewWithUiHandlers<PageUiHandlers> implements
		PagePresenter.MyView {

	interface PageViewUiBinder extends UiBinder<Widget, PageView> {
	}

	private static PageViewUiBinder uiBinder = GWT
			.create(PageViewUiBinder.class);

	public final Widget widget;

	@UiField
	SimplePanel specificContent;

	@Inject
	public PageView(final ClientState clientState) {
		widget = uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PagePresenter.TYPE_RevealSpecificContent) {
			specificContent.clear();
			specificContent.add(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

}
