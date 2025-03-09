package toady.fletching.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;

public class EchoExplosionBehaviour extends ExplosionBehavior {
    @Override
    public boolean canDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float power) {
        return false;
    }

    @Override
    public boolean shouldDamage(Explosion explosion, Entity entity) {
        return false;
    }

    @Override
    public float getKnockbackModifier(Entity entity) {
        return 1.75f;
    }
}
