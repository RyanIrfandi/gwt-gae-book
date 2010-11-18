package org.gwtgaebook.CultureShows.server.dao;

import java.util.logging.Logger;

import org.gwtgaebook.CultureShows.server.dispatch.DispatchActionHandler;
import org.gwtgaebook.CultureShows.server.util.Validation;

import com.google.appengine.api.datastore.Key;
import com.google.code.twig.ObjectDatastore;

/*
 * DAO providing CRUD
 */
public abstract class DAO<T> {
	protected Class<T> tClass;
	protected final ObjectDatastore datastore;

	protected static final Logger logger = Logger
			.getLogger(DAO.class.getName());

	protected DAO(Class<T> tClass, final ObjectDatastore datastore) {
		this.tClass = tClass;
		this.datastore = datastore;
	}

	public void validateKeySameAsClass(Key key) {
		// getCanonicalName().replaceAll("\\.", "_")
		String name = tClass.getName();
		if (name.lastIndexOf('.') > 0) {
			name = name.substring(name.lastIndexOf('.') + 1);
		}
		if (!name.equals(key.getKind())) {
			throw new IllegalArgumentException("Key kind " + key.getKind()
					+ " != class " + name);
		}
	}

	public Key getKey(T entity) {
		return datastore.associatedKey(entity);
	}

	public Key create(T entity) {
		// store creates a Key in the datastore and keeps it in the
		// ObjectDatastore associated with this theater instance.
		// Basically, every OD has a Map<Object, Key> which is used to look up
		// the Key for every operation.
		return datastore.store(entity);
	}

	public T read(Key key) {
		validateKeySameAsClass(key);
		return datastore.load(key);
	}

	public T read(String key) {
		return read(Validation.getValidDSKey(key));
	}

	public void update(T entity, Key key) {
		validateKeySameAsClass(key);
		datastore.associate(entity, key);
		datastore.update(entity);
	}

	public void delete(Key key) {
		validateKeySameAsClass(key);
		// workaround for
		// http://code.google.com/p/twig-persist/issues/detail?id=47
		T entity = datastore.load(key);
		datastore.getService().delete(key);
		datastore.disassociate(entity);
	}

}