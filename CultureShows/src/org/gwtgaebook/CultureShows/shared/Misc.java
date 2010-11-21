package org.gwtgaebook.CultureShows.shared;

public class Misc {
	public static String minutesToHHMM(int minutes) {
		int h = minutes / 60;
		int m = minutes % 60;
		return (h < 10 ? "0" : "") + Integer.toString(h) + ":"
				+ (m < 10 ? "0" : "") + Integer.toString(m);
	}
}
