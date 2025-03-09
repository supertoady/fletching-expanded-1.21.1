package toady.fletching.mixin;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import toady.fletching.FletchingExpanded;
import toady.fletching.effect.ModStatusEffects;

import java.util.Set;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileEntityMixin extends ProjectileEntity {
    @Shadow public abstract ItemStack getItemStack();
    @Shadow public abstract ItemStack getWeaponStack();

    @Shadow public PersistentProjectileEntity.PickupPermission pickupType;

    @Shadow public abstract boolean isCritical();

    @Unique protected boolean isExplosive;
    @Unique protected boolean isStreakProjectile;
    //contains explosive, quiver pickup code
    public PersistentProjectileEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    protected void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("isExplosive", this.isExplosive);
        nbt.putBoolean("isStreakProjectile", this.isStreakProjectile);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    protected void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.isExplosive = nbt.getBoolean("isExplosive");
        this.isStreakProjectile = nbt.getBoolean("isStreakProjectile");
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        if (!this.getWorld().isClient) {
            if (this.isExplosive) {
                FletchingExpanded.showParticlesToAll(this.getWorld(), ParticleTypes.SMOKE, this.getPos(), 0.05, 1, 0.01);
            }
            if (this.isStreakProjectile) {
                FletchingExpanded.showParticlesToAll(this.getWorld(), new DustParticleEffect(new Vector3f(0.502f, 0.878f, 1.0f), 1), this.getPos(), 0.05, 1, 0.01);
            }
            if (this.isFrozen()) {
                FletchingExpanded.showParticlesToAll(this.getWorld(), ParticleTypes.SNOWFLAKE, this.getPos(), 0.05, 1, 0.005);
            }
            if (this.isOnFire()) {
                FletchingExpanded.showParticlesToAll(this.getWorld(), ParticleTypes.SMALL_FLAME, this.getPos(), 0.05, 1, 0.005);
            }
        }
    }

    @Inject(method = "onEntityHit", at = @At("RETURN"))
    public void onHit(EntityHitResult entityHitResult, CallbackInfo ci){
        //make streak work
        if (this.getWeaponStack() != null && this.isCritical()){
            Set<RegistryEntry<Enchantment>> enchantments = this.getWeaponStack().getEnchantments().getEnchantments();
            if (this.getOwner() != null && this.getOwner() instanceof LivingEntity living){
                boolean bl = false;
                for (StatusEffectInstance instance : living.getStatusEffects()){
                    if (instance.getEffectType().equals(ModStatusEffects.STREAK)) bl = true;
                }

                for (RegistryEntry<Enchantment> entry : enchantments){
                    if (entry.getIdAsString().equals("fletching-expanded:streak")){
                        int duration = this.getWorld().getGameRules().getInt(FletchingExpanded.STREAK_DURATION) * 20;
                        if (bl) living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STREAK, duration, 1));
                        living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STREAK, duration, 0));
                    }
                }
            }
        }

        boolean bl = entityHitResult.getEntity().getType() == EntityType.ENDERMAN;
        if (this.isFrozen() && !bl) {
            entityHitResult.getEntity().setFrozenTicks(260);
        }

        if (this.isExplosive){
            World world = this.getWorld();
            world.createExplosion(this, Explosion.createDamageSource(world, this), null, this.getPos(), world.getGameRules().getInt(FletchingExpanded.EXPLOSIVE_POWER), this.isOnFire(), World.ExplosionSourceType.MOB);

            PotionContentsComponent potionContentsComponent = this.getPotionContents();
            if (potionContentsComponent.potion().isPresent()){
                applyLingeringPotion(potionContentsComponent);
            }
            this.kill();
        }
    }

    @Inject(method = "onBlockHit", at = @At("RETURN"))
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci){
        if (this.isExplosive){
            World world = this.getWorld();
            world.createExplosion(this, Explosion.createDamageSource(world, this), null, blockHitResult.getPos(), world.getGameRules().getInt(FletchingExpanded.EXPLOSIVE_POWER), this.isOnFire(), World.ExplosionSourceType.MOB);

            PotionContentsComponent potionContentsComponent = this.getPotionContents();
            if (potionContentsComponent.potion().isPresent()){
                applyLingeringPotion(potionContentsComponent);
            }
            this.kill();
        }
    }

    //unique methods
    @Unique
    private PotionContentsComponent getPotionContents() {
        return this.getItemStack().getOrDefault(DataComponentTypes.POTION_CONTENTS, PotionContentsComponent.DEFAULT);
    }

    @Unique
    private void applyLingeringPotion(PotionContentsComponent potion) {
        AreaEffectCloudEntity areaEffectCloudEntity = new AreaEffectCloudEntity(this.getWorld(), this.getX(), this.getY(), this.getZ());
        Entity var4 = this.getOwner();
        if (var4 instanceof LivingEntity livingEntity) {
            areaEffectCloudEntity.setOwner(livingEntity);
        }

        areaEffectCloudEntity.setRadius(1.0F);
        areaEffectCloudEntity.setRadiusOnUse(-0.5F);
        areaEffectCloudEntity.setWaitTime(10);
        areaEffectCloudEntity.setRadiusGrowth(-areaEffectCloudEntity.getRadius() / (float)areaEffectCloudEntity.getDuration());
        areaEffectCloudEntity.setPotionContents(potion);
        this.getWorld().spawnEntity(areaEffectCloudEntity);
    }
}