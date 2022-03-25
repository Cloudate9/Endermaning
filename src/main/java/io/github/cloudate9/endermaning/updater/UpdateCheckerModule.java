package io.github.cloudate9.endermaning.updater;

import dagger.Binds;
import dagger.Module;

import javax.inject.Singleton;

@Module
public interface UpdateCheckerModule {

    @Binds
    @Singleton
    UpdateChecker updateChecker(EndermaningUpdateChecker endermaningUpdateChecker);

}
