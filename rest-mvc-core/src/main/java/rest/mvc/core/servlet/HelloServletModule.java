package rest.mvc.core.servlet;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import rest.mvc.core.api.model.GuicyInterface;
import rest.mvc.core.api.model.GuicyInterfaceImpl;
import rest.mvc.core.api.services.HelloGuice;

import com.google.inject.Scopes;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class HelloServletModule extends JerseyServletModule {
	@Override
	protected void configureServlets() {
		// Must configure at least one JAX-RS resource or the
		// server will fail to start.
		bind(HelloGuice.class);
		bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);

		bind(GuiceContainer.class);

		bind(JacksonJsonProvider.class).in(Scopes.SINGLETON);

		// Route all requests through GuiceContainer
		serve("/*").with(GuiceContainer.class);
	}
}
