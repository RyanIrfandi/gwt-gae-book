package org.gwtgaebook.CultureShows.shared;

public class Constants {
	public static final String securityCookieName = "security";
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm";
	public static final String serverDateFormat = "yyyy-MM-dd HH:mm z";
	public static final String serverTimeZone = "GMT";

	public static final String anonymousUserPrefix = "anonymous-";
	public static final String defaultTheaterName = "My Theater";

	public static final Integer visibleRangeStart = 0;
	public static final Integer visibleRangeLength = 50;

	public enum ManageActionType {
		CREATE, READ, UPDATE, DELETE
	};

	public static long oneDayMiliseconds = 24 * 60 * 60 * 1000;
}
