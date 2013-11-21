package rest.mvc.core.controller;

import java.net.BindException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import rest.mvc.core.configuration.Configuration;
import rest.mvc.core.servlet.HelloServletModule;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.servlet.GuiceFilter;

@Singleton
public class HelloServer {
	private static final Logger LOG = LoggerFactory.getLogger(HelloServer.class);

	private static I18n i18n = I18nFactory.getI18n(HelloServer.class);

	private Server server = null;

	final String CONTEXT_PATH = "/";

	Configuration configs;

	Injector injector;

	@Inject
	public HelloServer(Configuration configs, Injector injector) {
		this.configs = configs;
		this.injector = injector;
	}

	public void run() throws Exception {
		// Create the server.
		server = new Server();
		try {
			ClassPathXmlApplicationContext parentSpringAppContext = new ClassPathXmlApplicationContext();
			parentSpringAppContext.refresh();
			ConfigurableListableBeanFactory factory = parentSpringAppContext.getBeanFactory();
			factory.registerSingleton("guiceInjector", injector);

			String[] contextLocations = {};
			ClassPathXmlApplicationContext springAppContext = new ClassPathXmlApplicationContext(contextLocations,
					parentSpringAppContext);

			ServletContextHandler root = new ServletContextHandler(server, CONTEXT_PATH, ServletContextHandler.SECURITY
					| ServletContextHandler.SESSIONS);

			// Add our Guice listener that includes our bindings
			// root.addEventListener(new HelloGuiceServletConfig());

			((HashSessionManager) root.getSessionHandler().getSessionManager()).setSessionCookie("RESTMVCSESSIONID");

			GenericWebApplicationContext springWebAppContext = new GenericWebApplicationContext();
			springWebAppContext.setServletContext(root.getServletContext());
			springWebAppContext.setParent(springAppContext);
			root.getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
					springWebAppContext);

			/* Configure web app context */
			root.setResourceBase(configs.getWebAppDir());

			// Then add GuiceFilter and configure the server to
			// reroute all requests through this filter.
			root.addFilter(GuiceFilter.class, "/*", null);

			// Must add DefaultServlet for embedded Jetty.
			// Failing to do this will cause 404 errors.
			// This is not needed if web.xml is used instead.
			ServletHolder rootServlet = root.addServlet(DefaultServlet.class, "/");
			rootServlet.setInitOrder(1);

			server.setThreadPool(new QueuedThreadPool(25));

			SelectChannelConnector apiConnector = new SelectChannelConnector();
			apiConnector.setPort(8282);
			apiConnector.setMaxIdleTime(900000);

			server.addConnector(apiConnector);

			server.setStopAtShutdown(true);
			springAppContext.start();

			// Start the server
			server.start();
			server.join();
		} catch (BindException bindException) {
			LOG.error(i18n.tr("Could not bind to server port - instance may already be running. Terminating this instance."),
					bindException);
			throw bindException;
		}
	}

	public void stop() throws Exception {
		try {
			server.stop();
		} catch (Exception e) {
			LOG.error(i18n.tr("Error stopping the server"), e);
		}
	}

	public static void main(String[] args) throws Exception {

		Configuration configs = new Configuration();

		Injector injector = Guice.createInjector(new HelloServletModule(configs));
		HelloServer server = null;
		try {
			LOG.info(i18n.tr("Getting the controller"));
			server = injector.getInstance(HelloServer.class);
			if (server != null) {
				server.run();
			}
		} catch (Throwable t) {
			LOG.error(i18n.tr("Failed to run Hello Server"), t);
			if (server != null) {
				server.stop();
			}
			System.exit(-1);
		}
	}
}
