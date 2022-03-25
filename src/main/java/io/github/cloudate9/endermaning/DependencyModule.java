package io.github.cloudate9.endermaning;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

@Module
public interface DependencyModule {

    @Provides
    @Reusable
    static MiniMessage miniMessage() {
        return MiniMessage.builder().build();
    }

    @Provides
    static JavaPlugin plugin() {
        return Endermaning.getProvidingPlugin(Endermaning.class);
    }

}
