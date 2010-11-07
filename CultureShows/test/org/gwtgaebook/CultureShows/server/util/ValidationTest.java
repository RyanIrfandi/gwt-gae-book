package org.gwtgaebook.CultureShows.server.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class ValidationTest {

	@Test
	public final void getValidDSKeyTest() throws Exception {
		try {
			Validation.getValidDSKey(null);
			fail("Null key was considered valid");
		} catch (IllegalArgumentException expected) {
			// exception thrown, we're good, nothing else to check here
		}

		try {
			Validation.getValidDSKey("");
			fail("Empty key was considered valid");
		} catch (IllegalArgumentException expected) {
			// exception thrown, we're good, nothing else to check here
		}

		final String invalidKey = "invalidKey";
		try {
			Validation.getValidDSKey(invalidKey);
			fail("Invalid key " + invalidKey + " was considered valid");
		} catch (IllegalArgumentException expected) {
			assertTrue("Exception message doesn't mention " + invalidKey,
					expected.getMessage().indexOf(invalidKey) >= 0);
		}

		// any appengine key will work
		final String validKey = "agxjdWx0dXJlc2hvd3NyDgsSB1RoZWF0ZXIY4V0M";
		Key dsKey;
		dsKey = Validation.getValidDSKey(validKey);
		assertTrue("Returned key is null", null != dsKey);
		assertTrue("Returned key " + dsKey.toString() + " is not equal to "
				+ validKey, dsKey.equals(KeyFactory.stringToKey(validKey)));

	}
}
