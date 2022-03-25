package io.github.cloudate9.endermaning.water;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigModule.class, DependencyModule.class, WaterCheckerModule.class})
public interface WaterCheckerComponent {

    WaterChecker getWaterChecker();

}
