package org.gwtgaebook.CultureShows.server;

import name.pehl.taoki.security.CookieSecurityCheck;
import name.pehl.taoki.security.RandomSecurityTokenGenerator;
import name.pehl.taoki.security.SecurityCheck;
import name.pehl.taoki.security.SecurityToken;
import name.pehl.taoki.security.SecurityTokenGenerator;
import name.pehl.taoki.security.SecurityTokenReader;
import name.pehl.taoki.security.UrlSecurityTokenReader;

import org.gwtgaebook.CultureShows.server.api.PerformancesResource;
import org.gwtgaebook.CultureShows.shared.Constants;

import com.google.inject.AbstractModule;

public class APIModule extends AbstractModule {

	@Override
	protected void configure() {
		// bindConstant().annotatedWith(SecurityToken.class).to(
		// Constants.securityCookieName);
		//
		// bind(SecurityCheck.class).to(CookieSecurityCheck.class);
		// bind(SecurityTokenGenerator.class).to(
		// RandomSecurityTokenGenerator.class);
		// bind(SecurityTokenReader.class).to(UrlSecurityTokenReader.class);

		bind(PerformancesResource.class);

	}
}
