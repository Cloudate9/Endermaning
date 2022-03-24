package io.github.cloudate9.endermaning.updater;

import dagger.Binds;
import dagger.Module;

@Module
public interface UpdateCheckerModule {

    @Binds
    UpdateChecker updateChecker(EndermaningUpdateChecker endermaningUpdateChecker);

}
