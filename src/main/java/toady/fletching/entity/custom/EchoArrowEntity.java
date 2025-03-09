package toady.fletching.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;
import toady.fletching.sound.ModSounds;

public class EchoArrowEntity extends PersistentProjectileEntity{

    public EchoArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        setDamage(2.5);
    }

    public EchoArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.ECHO_ARROW, owner, world, stack, shotFrom);
        setDamage(2.5);
    }

    public EchoArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.ECHO_ARROW, x, y, z, world, stack, shotFrom);
        setDamage(2.5);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.ECHO_ARROW);
    }

    @Override
    public void tick() {
        if (!this.getWorld().isClient){
            if (!this.inGround){
                FletchingExpanded.showParticlesToAll(this.getWorld(), ParticleTypes.SCULK_CHARGE_POP, this.getPos(), 0.05, 1, 0.005);
            }
        }
        super.tick();
    }

    @Override
    protected void onHit(LivingEntity target) {
        double i = this.getVelocity().length();
        FletchingExpanded.LOGGER.info("{}", i);
        target.addVelocity(0, 1f, 0);
        World world = this.getWorld();
        world.createExplosion(this, null, new EchoExplosionBehaviour(), this.getX(), this.getY(), this.getZ(),
                1.5f, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.SONIC_BOOM, ParticleTypes.SONIC_BOOM, ModSounds.ENTITY_ECHO_ARROW_HIT);
        super.onHit(target);
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        double i = this.getVelocity().length();
        FletchingExpanded.LOGGER.info("{}", i);
        World world = this.getWorld();
        world.createExplosion(this, null, new EchoExplosionBehaviour(), this.getX(), this.getY(), this.getZ(),
                1.5f, false, World.ExplosionSourceType.TRIGGER, ParticleTypes.SONIC_BOOM, ParticleTypes.SONIC_BOOM, ModSounds.ENTITY_ECHO_ARROW_HIT);
        super.onBlockHit(blockHitResult);
        this.kill();
    }
}
