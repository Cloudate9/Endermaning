package io.github.cloudate9.endermaning.commands;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;
import io.github.cloudate9.endermaning.teleporter.TeleporterModule;
import io.github.cloudate9.endermaning.water.WaterCheckerModule;

import javax.inject.Singleton;

@Component(modules = {
        CommandModule.class,
        ConfigModule.class,
        DependencyModule.class,
        TeleporterModule.class,
        WaterCheckerModule.class
})
@Singleton
public interface CommandComponent {

    SubCommandManager getSubCommandManager();

}
