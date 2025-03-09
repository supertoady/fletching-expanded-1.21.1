package toady.fletching.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.BowItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toady.fletching.effect.ModStatusEffects;

@Mixin(BowItem.class)
public class BowItemMixin {
    //streak effect makes you do more dmg
    @Inject(method = "shoot", at = @At("HEAD"))
    public void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, LivingEntity target, CallbackInfo ci){
        if (projectile instanceof PersistentProjectileEntity persistent){
            for (StatusEffectInstance instance : shooter.getStatusEffects()){
                if (instance.getEffectType() == ModStatusEffects.STREAK) {
                    NbtCompound nbt = new NbtCompound();
                    persistent.writeCustomDataToNbt(nbt);
                    nbt.putBoolean("isStreakProjectile", true);
                    persistent.readCustomDataFromNbt(nbt);

                    persistent.setDamage(persistent.getDamage() + instance.getAmplifier() + 2);
                    break;
                }
            }
        }
    }
}
