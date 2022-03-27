package io.github.ran.colorize;

import com.diogonunes.jcolor.Attribute;
import com.google.inject.Inject;
import org.apache.logging.log4j.Logger;
import space.vectrix.ignite.api.Platform;
import space.vectrix.ignite.api.event.Subscribe;
import space.vectrix.ignite.api.event.platform.PlatformConstructEvent;

import static com.diogonunes.jcolor.Ansi.colorize;
import static io.github.ran.colorize.Colorizer.windowsColorizer;

@SuppressWarnings({"ClassCanBeRecord", "FieldCanBeLocal", "unused"})
public class Colorize {
    private final Logger logger;
    private final Platform platform;

    @Inject
    public Colorize(Logger logger, Platform platform) {
        this.logger = logger;
        this.platform = platform;
    }

    @Subscribe
    public void onInitialize(PlatformConstructEvent event) {
        logger.info("Colorzing...");
        if (System.getProperty("os.name").startsWith("Windows")) windowsColorizer();
        logger.info(colorize("Colorized!", Attribute.BRIGHT_CYAN_TEXT()));
    }
}
