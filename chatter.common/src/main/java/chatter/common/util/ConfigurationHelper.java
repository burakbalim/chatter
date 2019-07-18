package chatter.common.util;

import chatter.common.exception.ChatterException;
import chatter.common.model.ChatterConfiguration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ConfigurationHelper {

    private static String configFile = "--configFile";

    public ChatterConfiguration getConfiguration(String[] args) throws ChatterException {

        Optional<String> config = Arrays.stream(args).filter(item -> item.startsWith(configFile)).findFirst();

        if (config.isPresent()) {
            String configFile = config.get().split(config + "=")[1];
            String file = ChatterUtil.readFile(configFile);
            return ChatterUtil.readJson(file, ChatterConfiguration.class);
        }

        return null;
    }

}
