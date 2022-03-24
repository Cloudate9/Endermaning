package io.github.cloudate9.endermaning.config;

import com.github.secretx33.sccfg.Config;
import dagger.Module;
import dagger.Provides;

@Module
public interface ConfigModule {

    @Provides
    static HybridConfig hybridConfig() {
        return Config.getConfig(HybridConfig.class);
    }

    @Provides
    static MessagesConfig messagesConfig() {
        return Config.getConfig(MessagesConfig.class);
    }

    @Provides
    static OptionsConfig optionsConfig() {
        return Config.getConfig(OptionsConfig.class);
    }

}
