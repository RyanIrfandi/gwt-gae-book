package org.gwtgaebook.CultureShows.server;

import java.lang.reflect.Modifier;

import org.gwtgaebook.CultureShows.shared.Constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;

public class GsonProvider implements Provider<Gson> {

	public Gson get() {
		Gson gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC)
				.excludeFieldsWithoutExposeAnnotation()
				.setDateFormat(Constants.serverDateFormat).create();

		return gson;
	}
}
