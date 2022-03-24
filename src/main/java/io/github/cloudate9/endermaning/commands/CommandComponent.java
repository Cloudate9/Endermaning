package io.github.cloudate9.endermaning.commands;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;
import io.github.cloudate9.endermaning.teleporter.TeleporterModule;

@Component(modules = {CommandModule.class, ConfigModule.class, DependencyModule.class, TeleporterModule.class})
public interface CommandComponent {

    SubCommandManager getSubCommandManager();

}
