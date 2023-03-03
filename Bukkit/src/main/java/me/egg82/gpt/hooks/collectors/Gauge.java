package me.egg82.gpt.hooks.collectors;

import dev.cubxity.plugins.metrics.api.metric.collector.Collector;
import dev.cubxity.plugins.metrics.api.metric.data.GaugeMetric;
import dev.cubxity.plugins.metrics.api.metric.data.Metric;
import dev.cubxity.plugins.metrics.api.metric.store.DoubleAdderStore;
import dev.cubxity.plugins.metrics.api.metric.store.Store;
import dev.cubxity.plugins.metrics.api.metric.store.StoreFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Gauge implements Collector {
    private final String name;
    private final Map<String, String> labels;
    private final Store<Double> count;

    public Gauge(@NotNull String name) { this(name, Collections.emptyMap(), DoubleAdderStore.Factory); }

    public Gauge(@NotNull String name, @NotNull Map<@NotNull String, @NotNull String> labels) { this(name, labels, DoubleAdderStore.Factory); }

    public Gauge(@NotNull String name, @NotNull Map<@NotNull String, @NotNull String> labels, @NotNull StoreFactory<@NotNull Store<@NotNull Double>> valueStoreFactory) {
        this.name = name;
        this.labels = labels;
        this.count = valueStoreFactory.create();
    }

    @Override
    public @NotNull List<@NotNull Metric> collect() { return Collections.singletonList(new GaugeMetric(name, labels, count.get())); }

    public @NotNull Gauge inc() {
        count.add(1.0d);
        return this;
    }

    public @NotNull Gauge dec() {
        count.add(-1.0d);
        return this;
    }

    public @NotNull Gauge set(double value) {
        double current = count.get();
        count.add(value - current);
        return this;
    }

    public @NotNull Gauge plusAssign(double delta) {
        count.add(Math.abs(delta));
        return this;
    }

    public @NotNull Gauge plusAssign(@NotNull Number delta) {
        count.add(Math.abs(delta.doubleValue()));
        return this;
    }

    public @NotNull Gauge minusAssign(double delta) {
        count.add(Math.abs(delta) * -1.0d);
        return this;
    }

    public @NotNull Gauge minusAssign(@NotNull Number delta) {
        count.add(Math.abs(delta.doubleValue()) * -1.0d);
        return this;
    }
}
