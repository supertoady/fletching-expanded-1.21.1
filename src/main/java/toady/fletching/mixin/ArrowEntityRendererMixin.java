package toady.fletching.mixin;

import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toady.fletching.FletchingExpanded;

@Mixin(ArrowEntityRenderer.class)
public abstract class ArrowEntityRendererMixin extends ProjectileEntityRenderer<ArrowEntity> {

    //makes arrows render as flame/frost while on fire
    public ArrowEntityRendererMixin(EntityRendererFactory.Context context) {
        super(context);
    }

    @Unique
    private static final Identifier FLAME_TEXTURE = FletchingExpanded.id("textures/entity/projectiles/flame_arrow.png");
    @Unique
    private static final Identifier FROST_TEXTURE = FletchingExpanded.id("textures/entity/projectiles/frost_arrow.png");

    @Inject(method = "getTexture(Lnet/minecraft/entity/projectile/ArrowEntity;)Lnet/minecraft/util/Identifier;", at = @At("HEAD"), cancellable = true)
    public void getTexture(ArrowEntity arrowEntity, CallbackInfoReturnable<Identifier> cir){
        if (arrowEntity.isOnFire()){
            cir.setReturnValue(FLAME_TEXTURE);
        }
        if (arrowEntity.isFrozen()){
            cir.setReturnValue(FROST_TEXTURE);
        }
    }
}
