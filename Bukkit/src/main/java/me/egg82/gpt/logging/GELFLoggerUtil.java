package me.egg82.gpt.logging;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import flexjson.JSONSerializer;
import me.egg82.gpt.compress.GZIPCompressionStream;
import me.egg82.gpt.core.DoubleBuffer;
import me.egg82.gpt.json.transformers.InstantTransformer;
import me.egg82.gpt.logging.models.GELFSubmissionModel;
import me.egg82.gpt.utils.TimeUtil;
import me.egg82.gpt.web.WebRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GELFLoggerUtil {
    private static final Logger logger = LoggerFactory.getLogger(GELFLoggerUtil.class);
    private static final long RETRY_DELAY_ON_ERROR = TimeUnit.MINUTES.toMillis(10);

    private static ScheduledExecutorService workPool = null;

    private GELFLoggerUtil() { }

    private static final GZIPCompressionStream GZIP_COMPRESSION = new GZIPCompressionStream();

    private static final String GELF_ADDRESS = "https://logs.egg82.me:2096/gelf";

    private static final String SESSION_ID = UUID.randomUUID().toString();

    private static String plugin = null;
    private static String serverId = null;
    private static String pluginVersion = null;
    private static String platform = null;
    private static String platformVersion = null;

    private static volatile boolean initialized = false;
    private static volatile boolean sendErrors = false;
    private static long lastFailedAttempt = -1;
    private static int failedCount;

    private static final Object queueLockObj = new Object();
    private static final DoubleBuffer<GELFSubmissionModel> modelQueue = new DoubleBuffer<>();

    public static void setData(
            @NotNull String plugin,
            @NotNull UUID serverId,
            @NotNull String pluginVersion,
            @NotNull String platformName,
            @NotNull String platformVersion
    ) {
        GELFLoggerUtil.plugin = plugin;
        GELFLoggerUtil.serverId = serverId.toString();
        GELFLoggerUtil.pluginVersion = pluginVersion;
        GELFLoggerUtil.platform = platformName;
        GELFLoggerUtil.platformVersion = platformVersion;
    }

    public static void doSendErrors(boolean sendErrors) {
        if (workPool != null) {
            workPool.shutdown();
            try {
                if (!workPool.awaitTermination(4L, TimeUnit.SECONDS)) {
                    workPool.shutdownNow();
                }
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }

        GELFLoggerUtil.initialized = true;
        GELFLoggerUtil.sendErrors = sendErrors;
        if (sendErrors) {
            workPool = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat(
                    "GPTFriend_GELFLoggerUtil_%d").build());
            workPool.scheduleWithFixedDelay(GELFLoggerUtil::sendModels, 1L, 2L, TimeUnit.SECONDS);
        } else {
            modelQueue.getReadBuffer().clear();
            modelQueue.getWriteBuffer().clear();
        }
    }

    public static void close() {
        workPool.shutdown();
        try {
            if (!workPool.awaitTermination(4L, TimeUnit.SECONDS)) {
                workPool.shutdownNow();
            }
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    public static void send(int level, @Nullable String message) {
        sendModel(getModel(level, message));
    }

    public static void queue(int level, @Nullable String message) {
        if (!initialized || sendErrors) {
            modelQueue.getWriteBuffer().add(getModel(level, message));
        }
    }

    public static void send(int level, @Nullable String message, @NotNull Throwable ex) { sendModel(getModel(level, message, ex)); }

    public static void queue(int level, @Nullable String message, @NotNull Throwable ex) {
        if (!initialized || sendErrors) {
            modelQueue.getWriteBuffer().add(getModel(level, message, ex));
        }
    }

    private static GELFSubmissionModel getModel(int level, @Nullable String message) {
        GELFSubmissionModel retVal = new GELFSubmissionModel();
        retVal.setPlugin(plugin);
        retVal.setHost(serverId);
        retVal.setShortMessage(message != null ? message : "null");
        retVal.setLevel(level);
        retVal.setSession(SESSION_ID);
        retVal.setPluginVersion(pluginVersion);
        retVal.setPlatform(platform);
        retVal.setPlatformVersion(platformVersion);
        return retVal;
    }

    private static GELFSubmissionModel getModel(int level, @Nullable String message, @NotNull Throwable ex) {
        GELFSubmissionModel retVal = new GELFSubmissionModel();
        retVal.setPlugin(plugin);
        retVal.setHost(serverId);
        retVal.setShortMessage(message != null ? message : "null");
        try (StringWriter builder = new StringWriter(); PrintWriter writer = new PrintWriter(builder)) {
            ex.printStackTrace(writer);
            String str = builder.toString();
            retVal.setFullMessage(str.substring(0, str.length() - System.lineSeparator().length()));
        } catch (IOException ex2) {
            logger.error("{}: {}", ex2.getClass().getName(), ex2.getMessage(), ex2);
        }
        retVal.setLevel(level);
        retVal.setSession(SESSION_ID);
        retVal.setPluginVersion(pluginVersion);
        retVal.setPlatform(platform);
        retVal.setPlatformVersion(platformVersion);
        return retVal;
    }

    private static void sendModels() {
        if (!initialized || !sendErrors) {
            return;
        }

        synchronized (queueLockObj) {
            modelQueue.swapBuffers();

            GELFSubmissionModel model;
            while ((model = modelQueue.getReadBuffer().poll()) != null) {
                sendModel(model);
            }
        }
    }

    private static void sendModel(@NotNull GELFSubmissionModel model) {
        if (lastFailedAttempt != -1 && System.currentTimeMillis() - lastFailedAttempt < RETRY_DELAY_ON_ERROR || failedCount > 10) {
            // Just throw away the model to not clog up the queue when the site is down for too long
            return;
        }

        JSONSerializer modelSerializer = new JSONSerializer();
        modelSerializer.prettyPrint(false);
        modelSerializer.transform(new InstantTransformer(), Instant.class);

        try {
            WebRequest request = WebRequest.builder(new URL(GELF_ADDRESS))
                    .method(WebRequest.RequestMethod.POST)
                    .timeout(new TimeUtil.Time(5000L, TimeUnit.MILLISECONDS))
                    .userAgent("egg82/GELFLogger")
                    .header("Content-Type", "application/json")
                    .header("Content-Encoding", "gzip")
                    .outputData(GZIP_COMPRESSION.compress(modelSerializer.exclude("*.class").deepSerialize(model).getBytes(StandardCharsets.UTF_8)))
                    .build();
            HttpURLConnection conn = request.getConnection();
            int responseCode = conn.getResponseCode();
            if (responseCode != 202 && responseCode != 429) {
                throw new IOException("Did not get valid response from server (response code " + responseCode + ") \"" + WebRequest.getString(conn) + "\"");
            }

            lastFailedAttempt = -1;
            failedCount = 0;
        } catch (IOException ex) {
            if (lastFailedAttempt == -1) {
                logger.error("{}: {}", ex.getClass().getName(), ex.getMessage(), ex);
            } else {
                logger.warn("Failed to send error logs again, retrying in 10 minutes");
            }
            lastFailedAttempt = System.currentTimeMillis();
            if (++failedCount == 10) {
                logger.warn("Disabling error log sending until next restart");
            }
        }
    }
}
