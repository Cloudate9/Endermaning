package io.github.cloudate9.endermaning.listeners;

import dagger.Component;
import io.github.cloudate9.endermaning.DependencyModule;
import io.github.cloudate9.endermaning.config.ConfigModule;
import io.github.cloudate9.endermaning.teleporter.TeleporterModule;
import io.github.cloudate9.endermaning.water.WaterCheckerModule;

import javax.inject.Singleton;

@Singleton
@Component(modules = {ConfigModule.class, DependencyModule.class, TeleporterModule.class, WaterCheckerModule.class})
public interface ListenerComponent {

    HidePumpkinPlayers getHidePumpkinPlayers();
    HybridTeleportation getHybridTeleportation();
    OfflineHybridStatusInformer getOfflineHybridStatusInformer();
    SilkHands getSilkHands();
    StopEndermenHostility getStopEndermenHostility();
    TeleporterAllocator getTeleporterAllocator();
    WaterRegisterer getWaterRegisterer();

}
