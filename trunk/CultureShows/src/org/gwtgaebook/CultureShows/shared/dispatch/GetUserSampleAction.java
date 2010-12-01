package org.gwtgaebook.CultureShows.shared.dispatch;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

public class GetUserSampleAction extends
		UnsecuredActionImpl<GetUserSampleResult> {

	private String requestURI;

	public GetUserSampleAction(final String requestURI) {
		this.requestURI = requestURI;
	}

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private GetUserSampleAction() {
	}

	public String getRequestURI() {
		return requestURI;
	}

}