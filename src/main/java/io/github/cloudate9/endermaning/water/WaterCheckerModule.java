package io.github.cloudate9.endermaning.water;

import dagger.Binds;
import dagger.Module;

@Module
public interface WaterCheckerModule {

    @Binds
    WaterChecker waterChecker(HybridWaterChecker hybridWaterChecker);

}
