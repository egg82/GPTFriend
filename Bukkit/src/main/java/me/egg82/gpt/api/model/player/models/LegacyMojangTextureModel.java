package me.egg82.gpt.api.model.player.models;

import flexjson.JSON;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LegacyMojangTextureModel implements Serializable {
    private long timestamp = System.currentTimeMillis();
    @JSON(name = "profileId")
    private String profileId = "";
    @JSON(name = "profileName")
    private String profileName = "";
    @JSON(name = "signatureRequired")
    private boolean signatureRequired = false;
    private Map<String, LegacyMojangTexturePropertyModel> textures = new HashMap<>();

    public LegacyMojangTextureModel() { }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    @JSON(name = "profileId")
    public @NotNull String getProfileId() { return profileId; }

    @JSON(name = "profileId")
    public void setProfileId(@NotNull String profileId) { this.profileId = profileId; }

    @JSON(name = "profileName")
    public @NotNull String getProfileName() { return profileName; }

    @JSON(name = "profileName")
    public void setProfileName(@NotNull String profileName) { this.profileName = profileName; }

    @JSON(name = "signatureRequired")
    public boolean isSignatureRequired() { return signatureRequired; }

    @JSON(name = "signatureRequired")
    public void setSignatureRequired(boolean signatureRequired) { this.signatureRequired = signatureRequired; }

    public @NotNull Map<String, LegacyMojangTexturePropertyModel> getTextures() { return textures; }

    public void setTextures(@NotNull Map<String, LegacyMojangTexturePropertyModel> textures) { this.textures = textures; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LegacyMojangTextureModel)) {
            return false;
        }
        LegacyMojangTextureModel that = (LegacyMojangTextureModel) o;
        return timestamp == that.timestamp && signatureRequired == that.signatureRequired && profileId.equals(that.profileId) && profileName.equals(that.profileName) && textures
                .equals(that.textures);
    }

    @Override
    public int hashCode() { return Objects.hash(timestamp, profileId, profileName, signatureRequired, textures); }

    @Override
    public String toString() {
        return "LegacyMojangTextureModel{" +
                "timestamp=" + timestamp +
                ", profileId='" + profileId + '\'' +
                ", profileName='" + profileName + '\'' +
                ", signatureRequired=" + signatureRequired +
                ", textures=" + textures +
                '}';
    }

    public static class LegacyMojangTexturePropertyModel implements Serializable {
        private String url = "";

        public LegacyMojangTexturePropertyModel() { }

        public @NotNull String getUrl() { return url; }

        public void setUrl(@NotNull String url) { this.url = url; }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof LegacyMojangTexturePropertyModel)) {
                return false;
            }
            LegacyMojangTexturePropertyModel that = (LegacyMojangTexturePropertyModel) o;
            return url.equals(that.url);
        }

        @Override
        public int hashCode() { return Objects.hash(url); }

        @Override
        public String toString() {
            return "LegacyMojangTexturePropertyModel{" +
                    "url='" + url + '\'' +
                    '}';
        }
    }
}
