package rest.mvc.core.api.model;

import com.google.inject.Injector;

public interface GuicyInterface {
	public String get();
	public Injector getInjector();
}
