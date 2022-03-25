package io.github.cloudate9.endermaning.water;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface WaterCheckerModule {

    @Binds
    @Singleton
    WaterChecker waterChecker(HybridWaterChecker hybridWaterChecker);

}
