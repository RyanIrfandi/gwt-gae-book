package org.gwtgaebook.CultureShows.shared;

public class Constants {
	public static final String securityCookieName = "security";
	public static final String defaultDateFormat = "yyyy-MM-dd";
	public static final String serverDateFormat = "yyyy-MM-dd";
	public static final String serverTimeZone = "GMT";

	public static final String anonymousUserPrefix = "anonymous-";
	public static final String defaultTheaterName = "My Theater";

	public static final Integer visibleRangeStart = 0;
	public static final Integer visibleRangeLength = 50;

	public static final String PerformanceDateCookieName = "PerformanceDate";
	public static final String PerformanceTimeCookieName = "PerformanceTime";
	public static final String PerformanceLocationNameCookieName = "PerformanceLocationName";

	public enum ManageActionType {
		CREATE, READ, UPDATE, DELETE
	};

	public static long oneDayMiliseconds = 24 * 60 * 60 * 1000;
}
