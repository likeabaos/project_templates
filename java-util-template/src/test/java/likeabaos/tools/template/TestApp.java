package likeabaos.tools.template;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

import picocli.CommandLine;

public class TestApp {

    @Test
    public void testLoadingProperties() throws FileNotFoundException, IOException {
	String[] args = { "-c", "src/test/resources/sample_config.properties" };
	App app = new App();
	CommandLine CLI = new CommandLine(app);
	CLI.parseArgs(args);
	app.loadConfigFile();
	String value = app.getProperties().getProperty("tesing_sample_config_start");
	assertEquals("The first line of the sample config file should be 001", "001", value);

	value = app.getProperties().getProperty("testing_sample_config_end");
	assertEquals("The last line of the sample config file should be 999", "999", value);

	value = app.getProperties().getProperty("testing_sample_config_string");
	assertEquals("this is a test", value);

	value = app.getProperties().getProperty("testing_sample_config_boolean");
	assertEquals("false", value);
    }

    @Test
    public void testExceptionReturnCode() throws Exception {
	String[] args = { "-c", "lalaland/lalaland/abcdef.xyz" }; // this does not exist
	App app = new App();
	CommandLine CLI = new CommandLine(app);
	CLI.parseArgs(args);
	int retCode = app.call();
	assertEquals(1, retCode); // bad config path, so this will have an 1
    }

    @Test
    public void testGoodReturnCode() throws Exception {
	String[] args = { "-c", "src/test/resources/sample_config.properties" };
	App app = new App();
	CommandLine CLI = new CommandLine(app);
	CLI.parseArgs(args);
	int retCode = app.call();
	assertEquals(0, retCode);
    }
}
