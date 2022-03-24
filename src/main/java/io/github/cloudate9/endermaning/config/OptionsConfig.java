package io.github.cloudate9.endermaning.config;

import com.github.secretx33.sccfg.api.annotation.Comment;
import com.github.secretx33.sccfg.api.annotation.Configuration;
import com.github.secretx33.sccfg.api.annotation.NamedPath;

@Configuration("options.yml")
public class OptionsConfig {

    @Comment("The cooldown time needed in seconds when hybrids use their teleporter. Does not apply to vanilla pearls")
    public int hybridTeleportCooldown = 15;

    @Comment("If players wearing pumpkins will be invisible to all hybrids.")
    public boolean hidePumpkinPlayersFromHybrids = false;

    @Comment("If naturally spawned enderman will be attack hybrids.")
    public boolean endermenHostileToHybrids = false;

    @Comment("If hybrids can attack naturally spawned endermen")
    public boolean hybridsCanAttackEndermen = true;

    @Comment("If a hybrid wil take damage from using normal ender pearls")
    public int hybridPearlDamage = 0;

    @NamedPath(path = "teleporter", name = "enabled", comment = "If hybrids will be given a teleporter")
    public boolean teleporterEnabled = true;

    @NamedPath(path = "teleporter", name = "randomInsteadOfInfinite",
            comment = "If hybrids are given a random teleporter instead of an infinite one.")
    public boolean randomInsteadOfInfiniteTeleporter = false;

    @Comment("If a hybrid's hands should be silk touched")
    public boolean hybridHasSilkTouchHands = false;

    @NamedPath(path = "waterRainHurtHybrids", name = "enabled", comment = "If water and rain will hurt hybrids.")
    public boolean waterDamageEnabled = true;

    @NamedPath(path = "waterRainHurtHybrids.damagePerDifficulty", name = "peaceful", comment = "Damage done per 20 ticks.")
    public int waterPeacefulDamage = 0;

    @NamedPath(path = "waterRainHurtHybrids.damagePerDifficulty", name = "easy", comment = "Damage done per 20 ticks.")
    public int waterEasyDamage = 0;

    @NamedPath(path = "waterRainHurtHybrids.damagePerDifficulty", name = "normal", comment = "Damage done per 20 ticks.")
    public int waterNormalDamage = 0;

    @NamedPath(path = "waterRainHurtHybrids.damagePerDifficulty", name = "hard", comment = "Damage done per 20 ticks.")
    public int waterHardDamage = 0;

}
