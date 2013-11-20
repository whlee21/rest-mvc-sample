package rest.mvc.core.controller;

import rest.mvc.core.api.model.GuicyInterface;
import rest.mvc.core.api.model.GuicyInterfaceImpl;

import com.google.inject.AbstractModule;

public class HelloModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(GuicyInterface.class).to(GuicyInterfaceImpl.class);
	}

}
