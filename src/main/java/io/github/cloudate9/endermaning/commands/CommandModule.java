package io.github.cloudate9.endermaning.commands;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

import javax.inject.Named;

@Module
public interface CommandModule {

    @Binds
    @IntoSet
    SubCommand addHybridSubCommand(AddHybridSubCommand addHybridSubCommand);

    @Binds
    @IntoSet
    SubCommand damageKinSubCommand(DamageKinSubCommand damageKinSubCommand);

    @Binds
    @Named("defaultConsole")
    SubCommand defaultConsoleSubCommand(HelpSubCommand helpSubCommand);

    @Binds
    @Named("defaultPlayer")
    SubCommand defaultPlayerSubCommand(GuiSubCommand guiSubCommand);

    @Binds
    @IntoSet
    SubCommand guiSubCommand(GuiSubCommand guiSubCommand);

    @Binds
    @IntoSet
    SubCommand helpSubCommand(HelpSubCommand helpSubCommand);

    @Binds
    @IntoSet
    SubCommand hidePumpkinPlayersSubCommand(HidePumpkinPlayersSubCommand hidePumpkinPlayersSubCommand);

    @Binds
    @IntoSet
    SubCommand hostileEndermenSubCommand(HostileEndermenSubCommand hostileEndermenSubCommand);

    @Binds
    @IntoSet
    SubCommand listSubCommand(ListSubCommand listSubCommand);

    @Binds
    @IntoSet
    SubCommand pearlDamageSubCommand(PearlDamageSubCommand pearlDamageSubCommand);

    @Binds
    @IntoSet
    SubCommand reloadSubCommand(ReloadSubCommand reloadSubCommand);

    @Binds
    @IntoSet
    SubCommand removeHybridSubCommand(RemoveHybridSubCommand removeHybridSubCommand);

    @Binds
    @IntoSet
    SubCommand silkHandsSubCommand(SilkHandsSubCommand silkHandsSubCommand);

    @Binds
    @IntoSet
    SubCommand teleporterSubCommand(TeleporterSubCommand teleporterSubCommand);

    @Binds
    @IntoSet
    SubCommand waterDamageSubCommand(WaterDamageSubCommand waterDamageSubCommand);

}
