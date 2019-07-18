package chatter.common.util;

import chatter.common.exception.ChatterException;
import chatter.common.model.ChatterConfiguration;
import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.*;

public class ConfigurationHelperTest {

    @Test
    public void getConfiguration() throws ChatterException {

        ConfigurationHelper configurationHelper = new ConfigurationHelper();
        URL test = getClass().getClassLoader().getResource("test.json");

        assertNotNull(test);

        ChatterConfiguration configuration = configurationHelper.getConfiguration("--configFile=" + test.getFile());

        assertEquals(configuration.getPort().toString(), "2001");
    }
}