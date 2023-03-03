package me.egg82.gpt.hooks;

import dev.cubxity.plugins.metrics.api.UnifiedMetricsProvider;
import dev.cubxity.plugins.metrics.api.metric.MetricsManager;
import dev.cubxity.plugins.metrics.api.metric.collector.Collector;
import dev.cubxity.plugins.metrics.api.metric.collector.CollectorCollection;
import me.egg82.gpt.logging.GELFLogger;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UnifiedMetricsHookImpl extends AbstractUnifiedMetricsHook {
    private final Logger logger = new GELFLogger(LoggerFactory.getLogger(getClass()));

    public final CollectorCollection asyncCollection;
    public final CollectorCollection syncCollection;

    private final double[] messageBuckets = new double[] {
            1,
            2,
            3,
            4,
            5,
            6,
            7,
            8,
            9,
            10,
            15,
            20,
            25,
            30,
            35,
            40,
            45,
            50,
            55,
            60,
            65,
            70,
            75,
            80,
            85,
            90,
            95,
            100,
            150,
            200,
            250,
            300,
            350,
            400,
            450,
            500,
            550,
            600,
            650,
            700,
            750,
            800,
            850,
            900,
            950,
            1000,
            1500,
            2000,
            2500,
            3000,
            3500,
            4000,
            4500,
            5000,
            5500,
            6000,
            6500,
            7000,
            7500,
            8000,
            8500,
            9000,
            9500,
            10000,
            };

    private final double[] latencyBuckets = new double[] {
            1,
            10,
            20,
            30,
            40,
            50,
            60,
            70,
            80,
            90,
            100,
            150,
            200,
            250,
            300,
            350,
            400,
            450,
            500,
            550,
            600,
            650,
            700,
            750,
            800,
            850,
            900,
            950,
            1000,
            1500,
            2000,
            2500,
            3000,
            3500,
            4000,
            4500,
            5000,
            5500,
            6000,
            6500,
            7000,
            7500,
            8000,
            8500,
            9000,
            9500,
            10000,
            };

    public UnifiedMetricsHookImpl() {
        logger.debug("Loading {}", getClass().getName());

        this.asyncCollection = new AsyncUnifiedMetricsCollection();
        this.syncCollection = new SyncUnifiedMetricsCollection();
        PluginHooks.getHooks().add(this);
    }

    @Override
    public void unload() {
        logger.debug("Unloading {}", getClass().getName());
        MetricsManager manager = UnifiedMetricsProvider.get().getMetricsManager();
        manager.unregisterCollection(asyncCollection);
        manager.unregisterCollection(syncCollection);
    }

    @Override
    public void lightUnload() { logger.debug("Unloading (light) {}", getClass().getName()); }

    @Override
    public void lightReload() { logger.debug("Reloading (light) {}", getClass().getName()); }

    private static class AsyncUnifiedMetricsCollection implements CollectorCollection {
        private final List<@NotNull Collector> collectors = new ArrayList<>();

        private AsyncUnifiedMetricsCollection() { }

        @Override
        public @NotNull List<@NotNull Collector> getCollectors() { return collectors; }

        @Override
        public boolean isAsync() { return true; }

        @Override
        public void dispose() { collectors.clear(); }

        @Override
        public void initialize() {
            // Add collectors that will be updated async here
        }
    }

    private static class SyncUnifiedMetricsCollection implements CollectorCollection {
        private final List<@NotNull Collector> collectors = new ArrayList<>();

        private SyncUnifiedMetricsCollection() { }

        @Override
        public @NotNull List<@NotNull Collector> getCollectors() { return collectors; }

        @Override
        public boolean isAsync() { return false; }

        @Override
        public void dispose() { collectors.clear(); }

        @Override
        public void initialize() {
            // Add collectors that will be updated sync here
        }
    }
}
