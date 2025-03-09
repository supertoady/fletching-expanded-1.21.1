package toady.fletching.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;

import javax.swing.text.Position;

public class PhantomArrowEntity extends PersistentProjectileEntity{

    public PhantomArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        setDamage(2.5);
    }

    public PhantomArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.PHANTOM_ARROW, owner, world, stack, shotFrom);
        setDamage(2.5);
    }

    public PhantomArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.PHANTOM_ARROW, x, y, z, world, stack, shotFrom);
        setDamage(2.5);
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void tick() {
        World world = this.getWorld();
        if (!world.isClient){
            if (!this.inGround){
                FletchingExpanded.showParticlesToAll(world, ParticleTypes.WHITE_SMOKE, this.getPos(), 0.1, 1, 0);
                if (this.age > 200){
                    world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.NEUTRAL, 1, 1);
                    FletchingExpanded.showParticlesToAll(world, ParticleTypes.WHITE_SMOKE, this.getPos(), 0.15, 10, 0);
                    if (pickupType == PickupPermission.ALLOWED){
                        ItemEntity item = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), this.getItemStack().copyWithCount(1));
                        world.spawnEntity(item);
                    }
                    this.kill();
                }
            }
        }
        super.tick();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.PHANTOM_ARROW);
    }
}
