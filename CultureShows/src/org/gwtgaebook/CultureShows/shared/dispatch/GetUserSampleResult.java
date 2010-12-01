package org.gwtgaebook.CultureShows.shared.dispatch;

import com.gwtplatform.dispatch.shared.Result;

public class GetUserSampleResult implements Result {

	private String response;

	public GetUserSampleResult(final String response) {
		this.response = response;
	}

	/**
	 * For serialization only.
	 */
	@SuppressWarnings("unused")
	private GetUserSampleResult() {
	}

	public String getResponse() {
		return response;
	}

}