package io.github.cloudate9.endermaning.teleporter;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigModule.class, DependencyModule.class, TeleporterModule.class})
public interface TeleporterComponent {

    TeleporterObject getTeleporterManager();

}
