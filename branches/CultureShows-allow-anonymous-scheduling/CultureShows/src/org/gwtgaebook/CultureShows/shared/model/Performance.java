package org.gwtgaebook.CultureShows.shared.model;

import java.io.*;
import java.util.*;

import com.google.code.twig.annotation.*;

// needs to be Serializable to be sent over GWT-RPC as part of action/result
public class Performance implements Serializable {
	@Index
	public String showKey;
	@Index
	public String locationKey;
	@Index
	public Date date;

	// denormalized
	@Index
	public String theaterKey;
	public String showName;
	public String showWebsiteURL;
	public String locationName;
}
