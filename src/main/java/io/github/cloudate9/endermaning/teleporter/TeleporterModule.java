package io.github.cloudate9.endermaning.teleporter;

import dagger.Binds;
import dagger.Module;

@Module
public interface TeleporterModule {

    @Binds
    TeleporterObject teleporterObject(HybridTeleporterObject hybridTeleporterObject);

}
