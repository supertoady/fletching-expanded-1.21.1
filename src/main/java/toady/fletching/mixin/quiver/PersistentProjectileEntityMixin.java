package toady.fletching.mixin.quiver;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toady.fletching.item.ModItems;
import toady.fletching.item.custom.QuiverItem;
import toady.fletching.network.UpdateQuiverComponentsPayload;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow protected boolean inGround;

    @Shadow public abstract boolean isNoClip();

    @Shadow public int shake;

    @Shadow public PersistentProjectileEntity.PickupPermission pickupType;

    @Shadow protected abstract ItemStack asItemStack();

    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    //quiver pickup code VVV
    @Inject(method = "onPlayerCollision", at = @At("HEAD"), cancellable = true)
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci){
        if (!player.getWorld().isClient && (this.inGround || this.isNoClip()) && this.shake <= 0) {
            if (this.pickupType.ordinal() == 1){
                for(int i = 0; i < player.getInventory().size(); ++i) {
                    ItemStack inventoryStack = player.getInventory().getStack(i);
                    if (inventoryStack.isOf(ModItems.QUIVER)) {
                        ItemStack mainStack = QuiverItem.getMainStack(inventoryStack);
                        ItemStack secondaryStack = QuiverItem.getSecondaryStack(inventoryStack);
                        if (!(mainStack.getCount() == mainStack.getMaxCount() && secondaryStack.getCount() == secondaryStack.getMaxCount())){
                            QuiverItem.addProjectile(inventoryStack, this.asItemStack().copy());
                            ServerPlayNetworking.send(((ServerPlayerEntity) player), new UpdateQuiverComponentsPayload(i, inventoryStack));
                            player.sendPickup((PersistentProjectileEntity)(Object)this, 1);
                            this.discard();
                            ci.cancel();
                        }
                    }
                }
            }
        }
    }
}
