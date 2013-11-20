package rest.mvc.core.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import com.google.inject.Injector;
import com.google.inject.Singleton;

@Singleton
public class Configuration {

	public static final String CONFIG_FILE = "rest-mvc.properties";

	public static final String WEBAPP_DIR_NAME = "web";

	private static I18n i18n = I18nFactory.getI18n(Configuration.class);
	private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
	private Properties properties;

	private Injector injector;

	public static final String HOME_PATH = System.getProperty("home.dir");

	public Configuration() {
		this(readConfigFile());
	}

	public Configuration(Properties properties) {
		this.properties = properties;
	}

	public void setInjector(Injector injector) {
		this.injector = injector;
	}

	public Injector getInjector() {
		return injector;
	}

	private static Properties readConfigFile() {
		if (Configuration.HOME_PATH == null || "".equals(Configuration.HOME_PATH)) {
			LOG.error(i18n.tr("home.dir property can't be null."));
			System.exit(-1);
		}
		LOG.info("Home dir : " + Configuration.HOME_PATH);

		Properties properties = new Properties();

		// Get property file stream from classpath
		InputStream inputStream = Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

		if (inputStream == null)
			throw new RuntimeException(CONFIG_FILE + " not found in classpath");

		// load the properties
		try {
			properties.load(inputStream);
		} catch (FileNotFoundException fnf) {
			LOG.info("No configuration file " + CONFIG_FILE + " found in classpath.", fnf);
		} catch (IOException ie) {
			throw new IllegalArgumentException("Can't read configuration file " + CONFIG_FILE, ie);
		}

		return properties;
	}

	public String getWebAppDir() {
		LOG.info("Web App DIR : {}", generatePath(WEBAPP_DIR_NAME));
		return generatePath(WEBAPP_DIR_NAME);
	}

	private String generatePath(String dirName) {
		if (Configuration.HOME_PATH.endsWith(File.separator)) {
			return Configuration.HOME_PATH + dirName;
		} else {
			return Configuration.HOME_PATH + File.separator + dirName;
		}
	}

}
