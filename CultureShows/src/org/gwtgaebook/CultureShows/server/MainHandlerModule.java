package org.gwtgaebook.CultureShows.server;

import com.gwtplatform.dispatch.server.guice.*;
import org.gwtgaebook.CultureShows.shared.dispatch.*;
import org.gwtgaebook.CultureShows.server.dispatch.*;

public class MainHandlerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		// bind Actions to ActionHandlers and ActionValidators
		bindHandler(ScheduleShowAction.class, ScheduleShowHandler.class);
	}
}
