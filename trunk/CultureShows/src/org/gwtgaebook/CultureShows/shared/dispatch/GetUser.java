package org.gwtgaebook.CultureShows.shared.dispatch;

import java.util.*;

import org.gwtgaebook.CultureShows.shared.model.*;

import com.gwtplatform.annotation.*;

@GenDispatch(isSecure = false)
public class GetUser {
	@In(1)
	String requestURI;

	@Out(1)
	String errorText; // empty if success

	@Out(2)
	UserInfo userInfo;

	@Out(3)
	// theaters user has access to; key/name
	List<Theater> theaters = new ArrayList<Theater>();

}
