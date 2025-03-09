package toady.fletching.enchant;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;

public record ExplosiveEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final ExplosiveEnchantmentEffect INSTANCE = new ExplosiveEnchantmentEffect();
    public static final MapCodec<ExplosiveEnchantmentEffect> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity target, Vec3d pos) {
        if (target instanceof PersistentProjectileEntity projectile) {
            NbtCompound nbt = new NbtCompound();
            projectile.writeCustomDataToNbt(nbt);
            nbt.putBoolean("isExplosive", true);
            projectile.readCustomDataFromNbt(nbt);
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
