package me.egg82.gpt.logging.models;

import flexjson.JSON;
import me.egg82.gpt.utils.UUIDUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class GELFSubmissionModel implements Serializable {
    private String version = "1.1";
    private String host = UUIDUtil.EMPTY_UUID_STRING;
    @JSON(name = "short_message")
    private String shortMessage = "";
    @JSON(name = "full_message")
    private String fullMessage = null;
    private long timestamp = Instant.now().getEpochSecond();
    private int level = -1;
    @JSON(name = "_session")
    private String session = UUIDUtil.EMPTY_UUID_STRING;
    @JSON(name = "_plugin_version")
    private String pluginVersion = "unknown";
    @JSON(name = "_platform")
    private String platform = "unknown";
    @JSON(name = "_platform_version")
    private String platformVersion = "unknown";
    @JSON(name = "_os")
    private String os = System.getProperty("os.name");
    @JSON(name = "_plugin")
    private String plugin = "unknown";

    public GELFSubmissionModel() { }

    public @NotNull String getVersion() { return version; }

    public void setVersion(@NotNull String version) { this.version = version; }

    public @NotNull String getHost() { return host; }

    public void setHost(@NotNull String host) { this.host = host; }

    @JSON(name = "short_message")
    public @NotNull String getShortMessage() { return shortMessage; }

    @JSON(name = "short_message")
    public void setShortMessage(@NotNull String shortMessage) { this.shortMessage = shortMessage; }

    @JSON(name = "full_message")
    public @Nullable String getFullMessage() { return fullMessage; }

    @JSON(name = "full_message")
    public void setFullMessage(@Nullable String fullMessage) { this.fullMessage = fullMessage; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getLevel() { return level; }

    public void setLevel(int level) { this.level = level; }

    @JSON(name = "_session")
    public @NotNull String getSession() { return session; }

    @JSON(name = "_session")
    public void setSession(@NotNull String session) { this.session = session; }

    @JSON(name = "_plugin_version")
    public @NotNull String getPluginVersion() { return pluginVersion; }

    @JSON(name = "_plugin_version")
    public void setPluginVersion(@NotNull String pluginVersion) { this.pluginVersion = pluginVersion; }

    @JSON(name = "_platform")
    public @NotNull String getPlatform() { return platform; }

    @JSON(name = "_platform")
    public void setPlatform(@NotNull String platform) { this.platform = platform; }

    @JSON(name = "_platform_version")
    public @NotNull String getPlatformVersion() { return platformVersion; }

    @JSON(name = "_platform_version")
    public void setPlatformVersion(@NotNull String platformVersion) { this.platformVersion = platformVersion; }

    @JSON(name = "_os")
    public @NotNull String getOs() { return os; }

    @JSON(name = "_os")
    public void setOs(@NotNull String os) { this.os = os; }

    @JSON(name = "_plugin")
    public String getPlugin() { return plugin; }

    @JSON(name = "_plugin")
    public void setPlugin(String plugin) { this.plugin = plugin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GELFSubmissionModel)) {
            return false;
        }
        GELFSubmissionModel that = (GELFSubmissionModel) o;
        return timestamp == that.timestamp && level == that.level && version.equals(that.version) && host.equals(that.host) && shortMessage.equals(that.shortMessage) && Objects
                .equals(
                        fullMessage,
                        that.fullMessage
                ) && session.equals(that.session) && pluginVersion.equals(that.pluginVersion) && platform.equals(that.platform) && platformVersion.equals(that.platformVersion) && os
                .equals(that.os) && plugin.equals(that.plugin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                version,
                host,
                shortMessage,
                fullMessage,
                timestamp,
                level,
                session,
                pluginVersion,
                platform,
                platformVersion,
                os,
                plugin
        );
    }

    @Override
    public String toString() {
        return "GELFSubmissionModel{" +
                "version='" + version + '\'' +
                ", host='" + host + '\'' +
                ", shortMessage='" + shortMessage + '\'' +
                ", fullMessage='" + fullMessage + '\'' +
                ", timestamp=" + timestamp +
                ", level=" + level +
                ", session='" + session + '\'' +
                ", pluginVersion='" + pluginVersion + '\'' +
                ", platform='" + platform + '\'' +
                ", platformVersion='" + platformVersion + '\'' +
                ", os='" + os + '\'' +
                ", plugin='" + plugin + '\'' +
                '}';
    }
}
