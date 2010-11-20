package org.gwtgaebook.CultureShows.shared;

public class Constants {
	public static final String securityCookieName = "security";
	public static final String defaultDateFormat = "yyyy-MM-dd HH:mm";
	public static final String performanceDateCookieName = "performanceDate";
	public static final String performanceShowNameCookieName = "performanceShow";
	public static final String performanceLocationNameCookieName = "performanceLocation";

	public static final String anonymousUserPrefix = "anonymous-";
	public static final String defaultTheaterName = "My Theater";

	public static final Integer visibleRangeStart = 0;
	public static final Integer visibleRangeLength = 50;

	public enum ManageActionType {
		CREATE, READ, UPDATE, DELETE
	};

	public static long oneDayMiliseconds = 24 * 60 * 60 * 1000;
}
