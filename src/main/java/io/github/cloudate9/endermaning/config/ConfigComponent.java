package io.github.cloudate9.endermaning.config;

import dagger.Component;

@Component(modules = ConfigModule.class)
public interface ConfigComponent {

    HybridConfig getHybridConfig();

    MessagesConfig getMessagesConfig();

    OptionsConfig getOptionsConfig();

}
