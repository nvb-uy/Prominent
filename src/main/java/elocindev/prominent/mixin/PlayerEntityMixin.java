package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.mythicbosses.MythicBosses;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Mixin(ServerPlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("TAIL"))
    public void prominent$removeMythicBosses(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!(player.getWorld() instanceof ServerWorld world)) return;

        if (damageSource.getSource() instanceof LivingEntity possibleBoss) {
            if (MythicBosses.isMythicBoss(possibleBoss)) {

                if (MythicBosses.isOwnedByBoss(possibleBoss)) {
                    LivingEntity owner = MythicBosses.getOwnerOf(possibleBoss);
                    
                    if (owner != null) {
                        owner.remove(RemovalReason.DISCARDED);
                    }
                }

                possibleBoss.remove(RemovalReason.DISCARDED);

                player.sendMessage(Text.literal("The Faceless whispers: You're weak.").setStyle(Style.EMPTY.withColor(0xb86bbf).withItalic(true)));
                
                int finalLevel = MythicBosses.getMythicLevel(possibleBoss) - 1;
                if (finalLevel < 1) finalLevel = 1;

                ItemEntity item = new ItemEntity(possibleBoss.getWorld(), possibleBoss.getX(), possibleBoss.getY() + 32, possibleBoss.getZ(), MythicBosses.getVoidHourglass(finalLevel));
                possibleBoss.getWorld().spawnEntity(item);
            }
        }
    }
}