package elocindev.prominent.fabric_quilt.callbacks;

import elocindev.prominent.fabric_quilt.ProminentLoader;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.player.PlayerEntity;

public class CombatCallback {
    public static void register() { 
        // This makes a hard stop for oneshots in PvP. 
        // By default the value is 0.10, meaning that attacks can't be greater than 10% of the player's max health.
        // Configurable in prominent.json
        ServerLivingEntityEvents.ALLOW_DAMAGE.register((entity, source, amount) -> {
            if (entity instanceof PlayerEntity) {
                if (source.getAttacker() instanceof PlayerEntity) {
                    if (ProminentLoader.Config.enablePvpFactor) {
                        if (amount > (entity.getMaxHealth() * ProminentLoader.Config.combatFactorPvp)) {
                            float newAmount = entity.getMaxHealth() * ProminentLoader.Config.combatFactorPvp;
            
                            entity.damage(source, newAmount);
                            return false;
                        }
                    }
                }
            }
            
            return true;
        });
    }
}
