package rest.mvc.core.servlet;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rest.mvc.core.api.model.GuicyInterface;
import rest.mvc.core.api.model.GuicyInterfaceImpl;
import rest.mvc.core.api.services.HelloGuice;

import com.google.inject.Scopes;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class HelloServletModule extends JerseyServletModule {

	private static final Logger LOG = LoggerFactory.getLogger(HelloServletModule.class);
	private static final String JERSEY_CONFIG_PROPERTY_RESOURCECONFIGCLASS = "com.sun.jersey.config.property.resourceConfigClass";
	private static final String JERSEY_API_JSON_POJO_MAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";
	private static final String JERSEY_CONFIG_PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";

	@Override
	protected void configureServlets() {
		// Must configure at least one JAX-RS resource or the
		// server will fail to start.
		bind(HelloGuice.class);
		bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);

		bind(GuiceContainer.class);

		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(JERSEY_CONFIG_PROPERTY_RESOURCECONFIGCLASS, "com.sun.jersey.api.core.PackagesResourceConfig");
		params.put(JERSEY_CONFIG_PROPERTY_PACKAGES, "rest.mvc.core.api.services");
		params.put(JERSEY_API_JSON_POJO_MAPPING_FEATURE, "true");
		// Route all requests through GuiceContainer
		serve("/*").with(GuiceContainer.class, params);

		params = new HashMap<String, String>();
		params.put(JERSEY_CONFIG_PROPERTY_RESOURCECONFIGCLASS, "com.sun.jersey.api.core.PackagesResourceConfig");
		params.put(JERSEY_CONFIG_PROPERTY_PACKAGES, "rest.mvc.core.api.services");
		params.put(JERSEY_API_JSON_POJO_MAPPING_FEATURE, "true");
		serve("/resources/*").with(GuiceContainer.class, params);
	}
}
