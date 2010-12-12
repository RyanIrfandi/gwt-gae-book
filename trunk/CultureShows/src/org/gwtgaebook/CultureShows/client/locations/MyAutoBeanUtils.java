package org.gwtgaebook.CultureShows.client.locations;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanCodex;
import com.google.gwt.autobean.shared.AutoBeanFactory;
import com.google.gwt.autobean.shared.AutoBeanUtils;

public class MyAutoBeanUtils<T> {

	AutoBeanFactory factory;
	Class<T> classType;

	public MyAutoBeanUtils(AutoBeanFactory factory, Class<T> classType) {
		this.factory = factory;
		this.classType = classType;
	}

	public String serializeToJson(T delegate) {
		AutoBean<T> bean = AutoBeanUtils.getAutoBean(delegate);
		return AutoBeanCodex.encode(bean).getPayload();
	}

	public T deserializeFromJson(String json) {
		AutoBean<T> bean = AutoBeanCodex.decode(factory, classType, json);
		return bean.as();
	}

}
