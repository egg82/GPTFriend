package me.egg82.gpt.logging;

import me.egg82.gpt.config.ConfigUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;

public class GELFLogger implements Logger {
    private final Logger impl;

    public GELFLogger(@NotNull Logger impl) {
        this.impl = impl;
    }

    @Override
    public String getName() { return impl.getName(); }

    @Override
    public boolean isTraceEnabled() { return impl.isTraceEnabled(); }

    @Override
    public void trace(String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(msg);
        }
    }

    @Override
    public void trace(String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(format, arg);
        }
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(format, arg1, arg2);
        }
    }

    @Override
    public void trace(String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(format, arguments);
        }
    }

    @Override
    public void trace(String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.trace(msg, t);
            } else {
                impl.trace(msg);
            }
        }
    }

    @Override
    public boolean isTraceEnabled(Marker marker) { return impl.isTraceEnabled(marker); }

    @Override
    public void trace(Marker marker, String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(marker, msg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(marker, format, arg);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(marker, format, arg1, arg2);
        }
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.trace(marker, format, argArray);
        }
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.trace(marker, msg, t);
            } else {
                impl.trace(marker, msg);
            }
        }
    }

    @Override
    public boolean isDebugEnabled() { return impl.isDebugEnabled(); }

    @Override
    public void debug(String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(msg);
        }
    }

    @Override
    public void debug(String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(format, arg);
        }
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(format, arg1, arg2);
        }
    }

    @Override
    public void debug(String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(format, arguments);
        }
    }

    @Override
    public void debug(String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.debug(msg, t);
            } else {
                impl.debug(msg);
            }
        }
    }

    @Override
    public boolean isDebugEnabled(Marker marker) { return impl.isDebugEnabled(marker); }

    @Override
    public void debug(Marker marker, String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(marker, msg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(marker, format, arg);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(marker, format, arg1, arg2);
        }
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.debug(marker, format, arguments);
        }
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.debug(marker, msg, t);
            } else {
                impl.debug(marker, msg);
            }
        }
    }

    @Override
    public boolean isInfoEnabled() { return impl.isInfoEnabled(); }

    @Override
    public void info(String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(msg);
        }
        GELFLoggerUtil.queue(1, msg);
    }

    @Override
    public void info(String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(format, arg);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(format, arg1, arg2);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void info(String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(format, arguments);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void info(String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.info(msg, t);
            } else {
                impl.info(msg);
            }
        }
        GELFLoggerUtil.queue(1, msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) { return impl.isInfoEnabled(marker); }

    @Override
    public void info(Marker marker, String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(marker, msg);
        }
        GELFLoggerUtil.queue(1, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(marker, format, arg);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(marker, format, arg1, arg2);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.info(marker, format, arguments);
        }
        GELFLoggerUtil.queue(1, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.info(marker, msg, t);
            } else {
                impl.info(marker, msg);
            }
        }
        GELFLoggerUtil.queue(1, msg, t);
    }

    @Override
    public boolean isWarnEnabled() { return impl.isWarnEnabled(); }

    @Override
    public void warn(String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(msg);
        }
        GELFLoggerUtil.queue(2, msg);
    }

    @Override
    public void warn(String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(format, arg);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(format, arg1, arg2);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void warn(String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(format, arguments);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void warn(String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.warn(msg, t);
            } else {
                impl.warn(msg);
            }
        }
        GELFLoggerUtil.queue(2, msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) { return impl.isWarnEnabled(marker); }

    @Override
    public void warn(Marker marker, String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(marker, msg);
        }
        GELFLoggerUtil.queue(2, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(marker, format, arg);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(marker, format, arg1, arg2);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.warn(marker, format, arguments);
        }
        GELFLoggerUtil.queue(2, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.warn(marker, msg, t);
            } else {
                impl.warn(marker, msg);
            }
        }
        GELFLoggerUtil.queue(2, msg, t);
    }

    @Override
    public boolean isErrorEnabled() { return impl.isErrorEnabled(); }

    @Override
    public void error(String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(msg);
        }
        GELFLoggerUtil.queue(3, msg);
    }

    @Override
    public void error(String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(format, arg);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(format, arg1, arg2);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void error(String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(format, arguments);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void error(String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.error(msg, t);
            } else {
                impl.error(msg);
            }
        }
        GELFLoggerUtil.queue(3, msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) { return impl.isErrorEnabled(marker); }

    @Override
    public void error(Marker marker, String msg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(marker, msg);
        }
        GELFLoggerUtil.queue(3, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(marker, format, arg);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.format(format, arg).getMessage());
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(marker, format, arg1, arg2);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.format(format, arg1, arg2).getMessage());
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        if (!ConfigUtil.getSilentOrFalse()) {
            impl.error(marker, format, arguments);
        }
        GELFLoggerUtil.queue(3, MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        if (!ConfigUtil.getSilentOrFalse()) {
            if (ConfigUtil.getDebugOrFalse()) {
                impl.error(marker, msg, t);
            } else {
                impl.error(marker, msg);
            }
        }
        GELFLoggerUtil.queue(3, msg, t);
    }
}
