package elocindev.prominent.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import elocindev.prominent.mythicbosses.MythicBosses;
import elocindev.prominent.soulbinding.Soulbound;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "onDeath", at = @At("TAIL"))
    public void prominent$removeMythicBosses(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!(player.getWorld() instanceof ServerWorld)) return;

        if (damageSource.getSource() instanceof LivingEntity possibleBoss) {
            if (MythicBosses.isMythicBoss(possibleBoss)) {

                if (MythicBosses.isOwnedByBoss(possibleBoss)) {
                    LivingEntity owner = MythicBosses.getOwnerOf(possibleBoss);
                    
                    if (owner != null) {
                        owner.remove(RemovalReason.DISCARDED);
                    }
                }

                possibleBoss.remove(RemovalReason.DISCARDED);

                player.sendMessage(Text.literal("The Faceless whispers: You've failed. Embrace the void.").setStyle(Style.EMPTY.withColor(0xb86bbf).withItalic(true)));
                
                int finalLevel = MythicBosses.getMythicLevel(possibleBoss) - 1;
                if (finalLevel < 1) finalLevel = 1;

                ItemEntity item = new ItemEntity(possibleBoss.getWorld(), possibleBoss.getX(), possibleBoss.getY() + 32, possibleBoss.getZ(), MythicBosses.getVoidHourglass(finalLevel));
                possibleBoss.getWorld().spawnEntity(item);
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void prominent$tickSoulboundDamage(CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (!(player.getWorld() instanceof ServerWorld)) return;
        World world = player.getWorld();

        if (!world.isClient() && world.getTime() % 20 == 0) {
            for (ItemStack stack : player.getInventory().main) {
                if (!(stack.getItem() instanceof Soulbound))
                    continue;

                if (Soulbound.isSoulbinded(stack) && !Soulbound.isSoulbindedTo(stack, player) && !player.isCreative()) {
                    player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is soulbound to someone else.").setStyle(Style.EMPTY.withColor(Formatting.RED))), true);

                    player.damage(player.getDamageSources().genericKill(), player.getMaxHealth() * 0.35f);
                } else if (!world.isClient() && !Soulbound.isSoulbinded(stack)) {
                    Soulbound.soulbind(stack, player);
                    player.sendMessage(Text.empty().append(stack.getName()).append(Text.literal(" is now soulbound to you.").setStyle(Style.EMPTY.withColor(Formatting.GOLD))), false);
                }
            }
        }
    }
}