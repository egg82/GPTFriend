package me.egg82.gpt.api.model.player;

import flexjson.JSONDeserializer;
import me.egg82.gpt.api.model.player.models.LegacyMojangTextureModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Objects;

public class PlayerSkinImpl implements PlayerSkin {
    private final String json;
    private final String base64;
    private final String signature;

    private final Instant timestamp;
    private final String profileId;
    private final String profileName;
    private final String skinUrl;

    public PlayerSkinImpl(@NotNull String base64) { this(base64, null); }

    public PlayerSkinImpl(@NotNull String base64, @Nullable String signature) {
        this.base64 = base64;
        this.json = new String(Base64.getDecoder().decode(base64.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        this.signature = signature;

        JSONDeserializer<LegacyMojangTextureModel> modelDeserializer = new JSONDeserializer<>();
        LegacyMojangTextureModel model = modelDeserializer.deserialize(this.json, LegacyMojangTextureModel.class);
        this.timestamp = Instant.ofEpochMilli(model.getTimestamp());
        this.profileId = model.getProfileId();
        this.profileName = model.getProfileName();
        this.skinUrl = model.getTextures().get("SKIN").getUrl();
    }

    @Override
    public @NotNull String getBase64() { return base64; }

    @Override
    public @Nullable String getSignature() { return signature; }

    @Override
    public @NotNull String getJson() { return json; }

    @Override
    public @NotNull Instant getTimestamp() { return timestamp; }

    @Override
    public @NotNull String getProfileId() { return profileId; }

    @Override
    public @NotNull String getProfileName() { return profileName; }

    @Override
    public @NotNull String getSkinUrl() { return skinUrl; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PlayerSkinImpl)) {
            return false;
        }
        PlayerSkinImpl that = (PlayerSkinImpl) o;
        return base64.equals(that.base64) && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() { return Objects.hash(base64, signature); }

    @Override
    public String toString() {
        return "PlayerSkinImpl{" +
                "json='" + json + '\'' +
                ", base64='" + base64 + '\'' +
                ", signature='" + signature + '\'' +
                ", timestamp=" + timestamp +
                ", profileId='" + profileId + '\'' +
                ", profileName='" + profileName + '\'' +
                ", skinUrl='" + skinUrl + '\'' +
                '}';
    }
}
