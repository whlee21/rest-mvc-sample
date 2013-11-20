package rest.mvc.core.api.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import rest.mvc.core.api.model.GuicyInterface;

import com.google.inject.Inject;
import com.google.inject.Injector;

@Path("/helloguice")
public class HelloGuice {
	private final GuicyInterface gi;

	@Inject
	public HelloGuice(final GuicyInterface gi) {
		this.gi = gi;
	}

	@GET
	@Produces("text/plain")
	public String get(@QueryParam("x") String x) {
		Injector injector = gi.getInjector();
		return "Howdy Guice. "
				+ "Injected impl "
				+ gi.toString()
				+ ". Injected query parameter "
				+ (x != null ? "x = " + x : "x is not injected. " + injector != null ? "injector is not null"
						: "injector is null.");
	}
}