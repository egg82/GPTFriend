package me.egg82.gpt;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

public class BukkitLoader implements PluginLoader {
    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();
        resolver.addRepository(new RemoteRepository.Builder("papermc", "default", "https://repo.papermc.io/repository/maven-public/").build());
        resolver.addRepository(new RemoteRepository.Builder("egg82-nexus", "default", "https://nexus.egg82.me/repository/maven-public/").build());
        resolver.addRepository(new RemoteRepository.Builder("jitpack.io", "default", "https://jitpack.io").build());
        resolver.addRepository(new RemoteRepository.Builder("sponge", "default", "https://repo.spongepowered.org/maven/").build());

        resolver.addDependency(new Dependency(new DefaultArtifact("ninja.egg82:event-chain-api:${eventchain.api.version}"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("ninja.egg82:event-chain-bukkit:${eventchain.version}"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("net.kyori:event-api:${kyori-event.version}"), null)); // TODO: exclusions?

        resolver.addDependency(new Dependency(new DefaultArtifact("net.sf.flexjson:flexjson:${flexjson.version}"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("commons-net:commons-net:${commons-net.version}"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("com.github.ben-manes.caffeine:caffeine:${caffeine.version}"), null)); // TODO: exclusions?

        resolver.addDependency(new Dependency(new DefaultArtifact("cloud.commandframework:cloud-paper:${cloud.version}"), null)); // TODO: exclusions?
        resolver.addDependency(new Dependency(new DefaultArtifact("cloud.commandframework:cloud-minecraft-extras:${cloud.version}"), null)); // TODO: exclusions?

        resolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-yaml:${configurate.version}"), null)); // TODO: exclusions?

        resolver.addDependency(new Dependency(new DefaultArtifact("com.theokanning.openai-gpt3-java:service:${openai.version}"), null));

        classpathBuilder.addLibrary(resolver);
    }
}
