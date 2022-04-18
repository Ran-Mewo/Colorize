package io.github.ran.colorize.logger;

import io.github.ran.ranitils.ColorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.message.Message;

public class ColorizedFilter extends AbstractFilter {
    @Override
    public Result filter(LogEvent event) {
        if (ColorUtils.AnsiColorUtils.containsMinecraftColorCodes(event.getMessage().getFormattedMessage())) {
            modify(event);
            return Result.DENY;
        }
        return Result.NEUTRAL;
    }

    public void modify(LogEvent event) {
        event = new Log4jLogEvent.Builder(event).setMessage(convert(event.getMessage())).build();
        LogManager.getLogger(event.getLoggerName()).log(event.getLevel(), event.getMarker(), event.getMessage(), event.getThrown());
    }

    private Message convert(Message prevMessage) {
        return new Message() {
            @Override
            public String getFormattedMessage() {
                return ColorUtils.AnsiColorUtils.minecraftColorToAnsi(prevMessage.getFormattedMessage());
            }

            @Override
            public String getFormat() {
                return prevMessage.getFormat();
            }

            @Override
            public Object[] getParameters() {
                return prevMessage.getParameters();
            }

            @Override
            public Throwable getThrowable() {
                return prevMessage.getThrowable();
            }
        };
    }
}
