package toady.fletching.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StrayEntity.class)
public abstract class StrayEntityMixin extends AbstractSkeletonEntity {
    protected StrayEntityMixin(EntityType<? extends AbstractSkeletonEntity> entityType, World world) {
        super(entityType, world);
    }

    //make stray shoot frozen arrow
    @Inject(method = "createArrowProjectile", at = @At("HEAD"), cancellable = true)
    private void createArrowProjectile(ItemStack arrow, float damageModifier, ItemStack shotFrom, CallbackInfoReturnable<PersistentProjectileEntity> cir) {
        PersistentProjectileEntity persistentProjectileEntity = super.createArrowProjectile(arrow, damageModifier, shotFrom);
        persistentProjectileEntity.setFrozenTicks(1200);
        cir.setReturnValue(persistentProjectileEntity);
    }
}
