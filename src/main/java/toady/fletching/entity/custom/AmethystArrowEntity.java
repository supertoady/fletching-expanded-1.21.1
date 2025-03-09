package toady.fletching.entity.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;

import java.util.List;

public class AmethystArrowEntity extends PersistentProjectileEntity{

    public AmethystArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        setDamage(2.5);
    }

    public AmethystArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.AMETHYST_ARROW, owner, world, stack, shotFrom);
        setDamage(2.5);
    }

    public AmethystArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.AMETHYST_ARROW, x, y, z, world, stack, shotFrom);
        setDamage(2.5);
    }

    @Override
    protected void onHit(LivingEntity target) {
        World world = this.getWorld();
        if (!world.isClient){
            FletchingExpanded.showParticlesToAll(world, new ItemStackParticleEffect(ParticleTypes.ITEM, Items.AMETHYST_SHARD.getDefaultStack()), this.getPos(), 0.2, 20, 0.1);
            world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL);

            List<Entity> entities = world.getOtherEntities(null,
                    new Box(this.getX() + 1.5, this.getY() + 2, this.getZ() + 1.5, this.getX() - 1.5, this.getY() - 2, this.getZ() - 1.5));

            for (Entity entity : entities){
                if (entity instanceof LivingEntity livingEntity){
                    if (livingEntity != target && livingEntity != this.getOwner()){
                        livingEntity.damage(this.getDamageSources().arrow(this, this.getOwner()), 8);
                    }
                }
            }
        }
        super.onHit(target);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        if (!world.isClient){
            FletchingExpanded.showParticlesToAll(world, new ItemStackParticleEffect(ParticleTypes.ITEM, Items.AMETHYST_SHARD.getDefaultStack()), this.getPos(), 0.2, 20, 0.1);
            world.playSound(null, this.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL);

            List<Entity> entities = world.getOtherEntities(null,
                    new Box(this.getX() + 1.5, this.getY() + 2, this.getZ() + 1.5, this.getX() - 1.5, this.getY() - 2, this.getZ() - 1.5));

            for (Entity entity : entities){
                if (entity instanceof LivingEntity livingEntity){
                    if (livingEntity != this.getOwner()){
                        livingEntity.damage(this.getDamageSources().arrow(this, this.getOwner()), 8);
                    }
                }
            }
            this.kill();
        }
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.AMETHYST_ARROW);
    }
}
