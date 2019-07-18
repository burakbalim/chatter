package chatter.common.util;

import chatter.common.exception.ChatterException;
import chatter.common.model.ChatterConfiguration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ConfigurationHelper {

    private static String configFilePrefix = "--configFile";

    public ChatterConfiguration getConfiguration(String... args) throws ChatterException {

        Optional<String> configFilePath = Arrays.stream(args).filter(item -> item.startsWith(ConfigurationHelper.configFilePrefix)).findFirst();

        if (configFilePath.isPresent()) {
            String path = configFilePath.get().split(configFilePrefix + "=")[1];
            String file = ChatterUtil.readFile(path);
            return ChatterUtil.readJson(file, ChatterConfiguration.class);
        }

        return null;
    }

}
