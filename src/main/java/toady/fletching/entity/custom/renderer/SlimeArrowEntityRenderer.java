package toady.fletching.entity.custom.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.Identifier;
import toady.fletching.FletchingExpanded;

@Environment(EnvType.CLIENT)
public class SlimeArrowEntityRenderer extends ProjectileEntityRenderer<PersistentProjectileEntity> {
    public SlimeArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PersistentProjectileEntity entity) {
        return FletchingExpanded.id("textures/entity/projectiles/slime_arrow.png");
    }
}
