package rest.mvc.core.servlet;

import rest.mvc.core.configuration.Configuration;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class HelloGuiceServletConfig extends GuiceServletContextListener {

	Configuration configs;

	public HelloGuiceServletConfig(Configuration configs) {
		this.configs = configs;
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new HelloServletModule(configs));
	}
}