package org.gwtgaebook.CultureShows.shared;

public class Constants {
	public static final String securityCookieName = "security";
	public static final String defaultDateFormat = "yyyy-MM-dd hh:mm a";
	public static final String PerformanceDateCookieName = "performanceDate";
	public static final String PerformanceShowNameCookieName = "performanceShow";
	public static final String PerformanceLocationNameCookieName = "performanceLocation";

	public static final String anonymousUserPrefix = "anonymous-";
	public static final String defaultTheaterName = "My Theater";

	public static final Integer visibleRangeStart = 0;
	public static final Integer visibleRangeLength = 50;

	public enum manageActionType {
		CREATE, READ, UPDATE, DELETE
	};

}
