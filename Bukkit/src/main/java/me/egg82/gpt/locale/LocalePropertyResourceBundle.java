package me.egg82.gpt.locale;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Locale;
import java.util.PropertyResourceBundle;

public class LocalePropertyResourceBundle extends PropertyResourceBundle {
    private final Locale locale;

    public LocalePropertyResourceBundle(@NotNull InputStream stream, @NotNull Locale locale) throws IOException {
        super(stream);
        this.locale = locale;
    }

    public LocalePropertyResourceBundle(@NotNull Reader reader, @NotNull Locale locale) throws IOException {
        super(reader);
        this.locale = locale;
    }

    public LocalePropertyResourceBundle(@NotNull Locale locale, @NotNull String versionKey) throws IOException {
        super(new StringReader(versionKey + " = 1.0"));
        this.locale = locale;
    }

    @Override
    public @NotNull Locale getLocale() { return locale; }
}
