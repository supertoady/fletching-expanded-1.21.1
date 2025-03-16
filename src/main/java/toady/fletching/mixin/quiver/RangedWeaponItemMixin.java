package toady.fletching.mixin.quiver;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toady.fletching.item.ModItems;
import toady.fletching.item.custom.QuiverItem;

@Mixin(RangedWeaponItem.class)
public class RangedWeaponItemMixin {
    //ensure projectile is removed
    @Inject(method = "getProjectile", at = @At("HEAD"), cancellable = true)
    private static void getProjectile(ItemStack stack, ItemStack projectileStack, LivingEntity shooter, boolean multishot, CallbackInfoReturnable<ItemStack> cir){
        if (shooter instanceof PlayerEntity player && projectileStack.isIn(ItemTags.ARROWS)) {
            for(int i = 0; i < player.getInventory().size(); ++i) {
                ItemStack inventoryStack = player.getInventory().getStack(i);
                if (inventoryStack.isOf(ModItems.QUIVER)) {
                    if (!QuiverItem.getMainStack(inventoryStack).isEmpty()){
                        cir.setReturnValue(QuiverItem.removeNextProjectile(inventoryStack));
                    }
                }
            }
        }
    }
}
