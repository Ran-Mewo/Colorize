package io.github.ran.colorize.mixins;

import com.mojang.logging.LogUtils;
import io.github.ran.colorize.logger.ColorizedFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LogUtils.class)
public abstract class LogUtilsMixin {
    @Unique
    private static boolean colorized = false;

    @Inject(method = "getLogger", at = @At("HEAD"))
    private static void getLogger(CallbackInfoReturnable<Logger> cir) {
        if (!colorized) {
            org.apache.logging.log4j.core.LoggerContext ctx = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
            Configuration config = ctx.getConfiguration();
            LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
            loggerConfig.addFilter(new ColorizedFilter());
            ctx.updateLoggers();
            colorized = true;
        }
    }
}
