package likeabaos.tools.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.Option;

@Command(name = "Utility Template",
	mixinStandardHelpOptions = true,
	versionProvider = App.VersionProvider.class,
	description = "A template for Java utility program.")
public class App implements Callable<Integer> {
    private final static Logger LOG = LogManager.getLogger();
    private final static CommandLine CLI = new CommandLine(new App());

    public static void main(String[] args) throws Exception {
	int exitCode = CLI.execute(args);
	System.exit(exitCode);
    }

    @Option(names = { "-c", "config" }, required = true, description = "The full path to configuration file.")
    private File config_file;

    private Properties properties;

    @Override
    public Integer call() throws Exception {
	int errorCode = 0;
	try {
	    LOG.info("Program started");
	    this.loadConfigFile();
	    LOG.info("Starting process...");
	    Processor p = new Processor(this);
	    p.start();
	    LOG.info("Program completed");
	} catch (Exception e) {
	    LOG.error("Program encounter fatal error, not able to continue", e);
	    errorCode = 1;
	}
	return errorCode;
    }

    public void loadConfigFile() throws FileNotFoundException, IOException {
	if (this.config_file == null)
	    throw new IllegalStateException("Need to specify a config file. See help with -h option.");
	LOG.info("Loading configuration from file: {}", this.config_file.getAbsolutePath());
	try (FileReader reader = new FileReader(this.config_file)) {
	    this.properties = new Properties();
	    this.properties.load(reader);
	}
    }

    public Properties getProperties() {
	return this.properties;
    }

    public static class VersionProvider implements IVersionProvider {
	@Override
	public String[] getVersion() throws Exception {
	    try (InputStream input = this.getClass().getClassLoader().getResourceAsStream("build.properties")) {
		Properties prop = new Properties();
		prop.load(input);
		return new String[] { prop.getProperty("app_version") };
	    }
	}
    }
}
