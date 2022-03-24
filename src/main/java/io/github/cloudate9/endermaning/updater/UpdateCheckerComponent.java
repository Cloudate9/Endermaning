package io.github.cloudate9.endermaning.updater;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigModule.class, DependencyModule.class, UpdateCheckerModule.class})
public interface UpdateCheckerComponent {

    UpdateChecker getUpdateChecker();

}
