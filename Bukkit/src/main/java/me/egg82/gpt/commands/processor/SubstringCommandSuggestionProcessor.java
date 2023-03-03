package me.egg82.gpt.commands.processor;

import cloud.commandframework.execution.CommandSuggestionProcessor;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class SubstringCommandSuggestionProcessor<C> implements CommandSuggestionProcessor<C> {
    public SubstringCommandSuggestionProcessor() { }

    @Override
    public @NonNull List<String> apply(
            @NonNull CommandPreprocessingContext<C> context,
            @NonNull List<String> strings
    ) {
        if (context.getInputQueue().isEmpty()) {
            return strings;
        }

        List<String> retVal = new ArrayList<>();
        String input = context.getInputQueue().peek().toLowerCase();
        for (String s : strings) {
            if (s.toLowerCase().contains(input)) {
                retVal.add(s);
            }
        }
        return retVal;
    }
}
