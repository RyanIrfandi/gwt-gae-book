package org.gwtgaebook.template.server.util;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.repackaged.com.google.common.base.Strings;

public class Validation {

	public static Key getValidDSKey(String key) {
		if (Strings.isNullOrEmpty(key)) {
			throw new IllegalArgumentException("Null or empty key");
		}

		Key dsKey;
		try {
			dsKey = KeyFactory.stringToKey(key);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid key " + key);
		}

		return dsKey;
	}

}
