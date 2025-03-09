package toady.fletching.mixin.quiver;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toady.fletching.item.ModItems;
import toady.fletching.item.custom.QuiverItem;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Shadow @Final PlayerInventory inventory;

    //makes the player's prioritised projectile type be the quiver projectile
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "getProjectileType", at = @At("HEAD"), cancellable = true)
    public void getProjectileType(ItemStack stack, CallbackInfoReturnable<ItemStack> cir){
        if ((stack.getItem() instanceof RangedWeaponItem)) {

            ItemStack quiverStack = ItemStack.EMPTY;
            if (getOffHandStack().isOf(ModItems.QUIVER)) quiverStack = getOffHandStack();
            else if (getMainHandStack().isOf(ModItems.QUIVER)) quiverStack = getMainHandStack();

            if (quiverStack.isEmpty()) {
                for(int i = 0; i < this.inventory.size(); ++i) {
                    ItemStack inventoryStack = this.inventory.getStack(i);
                    if (inventoryStack.isOf(ModItems.QUIVER)) {
                        ItemStack quiverProjectile = QuiverItem.getMainStack(inventoryStack);
                        if (!quiverProjectile.isEmpty()){
                            cir.setReturnValue(quiverProjectile);
                        }
                    }
                }
            }
            else {
                ItemStack quiverProjectile = QuiverItem.getMainStack(quiverStack);
                if (!quiverProjectile.isEmpty()){
                    cir.setReturnValue(quiverProjectile);
                }
            }
        }
    }
}
