package org.gwtgaebook.CultureShows.client.locations;

import java.util.List;

import com.google.gwt.autobean.shared.AutoBean;
import com.google.gwt.autobean.shared.AutoBeanCodex;
import com.google.gwt.autobean.shared.AutoBeanFactory;
import com.google.gwt.autobean.shared.AutoBeanUtils;
import com.google.gwt.core.client.GWT;

public class LocationModel {
	// TODO generate the interface from a class?
	public interface Location {
		String getName();

		void setName(String name);
	}

	public interface Locations {
		List<Location> getLocations();

		void setLocations(List<Location> locations);
	}

	public interface LocationFactory extends AutoBeanFactory {
		AutoBean<Location> location();

		AutoBean<Locations> locations();
	}

	public static LocationFactory factory = GWT.create(LocationFactory.class);

	public static Location makeLocation() {
		AutoBean<Location> bean = factory.location();
		return bean.as();
	}

	public static Locations makeLocations() {
		AutoBean<Locations> bean = factory.locations();
		return bean.as();
	}

	public static String serializeToJson(Location location) {
		// Retrieve the AutoBean controller
		AutoBean<Location> bean = AutoBeanUtils.getAutoBean(location);

		return AutoBeanCodex.encode(bean).getPayload();
	}

	// "{\"locationKey\":\"agxjdWx0dXJlc2hvd3NyHQsSB1RoZWF0ZXIY9AIMCxIITG9jYXRpb24Y_gIM\",\"name\":\"l2\"}"
	public static Location deserializeFromJson(String json) {
		AutoBean<Location> bean = AutoBeanCodex.decode(factory, Location.class,
				json);
		return bean.as();
	}

	// public static Locations deserializeFromJson(String json) {
	// AutoBean<Locations> bean = AutoBeanCodex.decode(factory,
	// Locations.class, json);
	// return bean.as();
	// }

}
