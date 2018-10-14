package ru.context;

import org.aeonbits.owner.ConfigFactory;
import ru.sbtqa.tag.pagefactory.web.properties.WebConfiguration;

public final class Context {
    private static final WebConfiguration WEB_CONFIGURATION = ConfigFactory.create(WebConfiguration.class);

    private Context() {
    }

    public static WebConfiguration webConfig() {
        return WEB_CONFIGURATION;
    }
}
