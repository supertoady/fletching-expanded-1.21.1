package toady.fletching.entity.custom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;
import toady.fletching.sound.ModSounds;

import java.util.List;
import java.util.UUID;

public class HomingArrowEntity extends PersistentProjectileEntity{

    @Nullable private UUID targetUUID;
    @Nullable private Entity target;

    public HomingArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public HomingArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.HOMING_ARROW, owner, world, stack, shotFrom);
    }

    public HomingArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.HOMING_ARROW, x, y, z, world, stack, shotFrom);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.HOMING_ARROW);
    }

    public void setTarget(@Nullable Entity entity) {
        if (entity != null) {
            this.targetUUID = entity.getUuid();
            this.target = entity;
        }
    }

    @Nullable
    public Entity getTarget() {
        if (this.target != null && !this.target.isRemoved()) {
            return this.target;
        } else {
            if (this.targetUUID != null) {
                World var2 = this.getWorld();
                if (var2 instanceof ServerWorld serverWorld) {
                    this.target = serverWorld.getEntity(this.targetUUID);
                    return this.target;
                }
            }

            return null;
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        if (this.targetUUID != null) {
            nbt.putUuid("Target", this.targetUUID);
        }
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.containsUuid("Target")) {
            this.targetUUID = nbt.getUuid("Target");
            this.target = null;
        }
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void tick() {
        Entity target = getTarget();
        World world = this.getWorld();
        int hr = world.getGameRules().getInt(FletchingExpanded.HOMING_RANGE);
        if (!world.isClient && !this.inGround) FletchingExpanded.showParticlesToAll(world, ParticleTypes.PORTAL, this.getPos(), 0.2, 3, 0);
        if (target == null){
            List<Entity> entities = this.getWorld().getOtherEntities(this, new Box(this.getPos().subtract(hr, hr, hr), this.getPos().add(hr, hr, hr)));
            for (Entity entity : entities){
                if (entity != this.getOwner() && (entity instanceof LivingEntity || entity instanceof EndCrystalEntity)){
                    if (entity instanceof LivingEntity living && !living.isInCreativeMode()){
                        this.setTarget(entity);
                        if (!world.isClient){
                            world.playSound(null, this.getBlockPos(), ModSounds.ENTITY_HOMING_ARROW_TARGET, SoundCategory.NEUTRAL);
                            FletchingExpanded.showParticlesToAll(world, new ItemStackParticleEffect(ParticleTypes.ITEM, Items.ENDER_EYE.getDefaultStack()), this.getPos(), 0.1, 4, 0);
                        }
                        break;
                    }
                }
            }
        }
        else if (!this.inGround){
            Vec3d dir = (target.getEyePos().subtract(this.getPos())).normalize();
            double i = this.getVelocity().length();
            this.setVelocity(dir.multiply(Math.max(0.5, i)));
        }
        super.tick();
    }
}
