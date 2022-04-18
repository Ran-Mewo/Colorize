package io.github.ran.colorize;

import com.google.inject.Inject;
import io.github.ran.ranitils.shaded.com.diogonunes.jcolor.Attribute;
import org.apache.logging.log4j.Logger;
import space.vectrix.ignite.api.Platform;
import space.vectrix.ignite.api.event.Subscribe;
import space.vectrix.ignite.api.event.platform.PlatformConstructEvent;

import static io.github.ran.ranitils.ColorUtils.AnsiColorUtils.enableAnsiOnWindows;
import static io.github.ran.ranitils.shaded.com.diogonunes.jcolor.Ansi.colorize;

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
        if (System.getProperty("os.name").startsWith("Windows")) enableAnsiOnWindows();
        logger.info(colorize("Colorized!", Attribute.BRIGHT_CYAN_TEXT()));
    }
}
