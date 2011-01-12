package org.gwtgaebook.template.client;

import com.gwtplatform.mvp.client.annotations.*;

/**
 * The central location of all name tokens for the application. All
 * {@link ProxyPlace} classes get their tokens from here. This class also makes
 * it easy to use name tokens as a resource within UIBinder xml files.
 * <p />
 * This class uses both static variables and static getters. The reason for this
 * is that, if you want to use {@code NameTokens} in a UiBinder file, you can
 * only access static methods of the class. On the other hand, when you use the
 * {@literal @}{@link NameToken} annotation, you can only refer to a static
 * variable.
 * 
 */
public class NameTokens {

	public static final String landing = "!landing";

	public static String getLanding() {
		return landing;
	}

	public static final String signIn = "!signIn";

	public static String getSignIn() {
		return signIn;
	}

	public static final String signOut = "!signOut";

	public static String getSignOut() {
		return signOut;
	}

	public static final String locations = "!locations";

	public static String getLocations() {
		return locations;
	}

}
