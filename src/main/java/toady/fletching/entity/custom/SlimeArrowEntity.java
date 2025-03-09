package toady.fletching.entity.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;
import toady.fletching.sound.ModSounds;

import java.util.List;

public class SlimeArrowEntity extends PersistentProjectileEntity{
    protected int bounces;

    public SlimeArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.bounces = world.getGameRules().getInt(FletchingExpanded.SLIME_BOUNCES);
    }

    public SlimeArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.SLIME_ARROW, owner, world, stack, shotFrom);
        this.bounces = world.getGameRules().getInt(FletchingExpanded.SLIME_BOUNCES);
    }

    public SlimeArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.SLIME_ARROW, x, y, z, world, stack, shotFrom);
        this.bounces = world.getGameRules().getInt(FletchingExpanded.SLIME_BOUNCES);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putInt("bounces", this.bounces);
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.bounces = nbt.getInt("bounces");
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        World world = this.getWorld();
        Vec3d velocity = this.getVelocity();
        if (bounces > 0 && !world.getBlockState(blockHitResult.getBlockPos()).isOf(Blocks.TARGET)){
            if (!world.isClient){
                FletchingExpanded.showParticlesToAll(world, new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState()), this.getPos(), 0.1, 10, 0);
                world.playSound(null, this.getBlockPos(), ModSounds.ENTITY_SLIME_ARROW_HIT, SoundCategory.NEUTRAL);
                Direction hitDirection = blockHitResult.getSide();
                if (hitDirection.getAxis() == Direction.Axis.X){
                    this.setVelocity(velocity.x * -0.8, velocity.y * 0.8, velocity.z * 0.8);
                }
                else if (hitDirection.getAxis() == Direction.Axis.Y){
                    this.setVelocity(velocity.x * 0.8, velocity.y * -0.8, velocity.z * 0.8);
                }
                else if (hitDirection.getAxis() == Direction.Axis.Z){
                    this.setVelocity(velocity.x * 0.8, velocity.y * 0.8, velocity.z * -0.8);
                }
            }
            bounces--;
        }
        else {
            super.onBlockHit(blockHitResult);
        }
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.SLIME_ARROW);
    }
}
