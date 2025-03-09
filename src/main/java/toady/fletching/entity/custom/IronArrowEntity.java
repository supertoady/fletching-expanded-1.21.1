package toady.fletching.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.entity.ModEntityType;
import toady.fletching.item.ModItems;

public class IronArrowEntity extends PersistentProjectileEntity{


    public IronArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        setDamage(2.5);
    }

    public IronArrowEntity(World world, LivingEntity owner, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.IRON_ARROW, owner, world, stack, shotFrom);
        setDamage(2.5);
    }

    public IronArrowEntity(World world, double x, double y, double z, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(ModEntityType.IRON_ARROW, x, y, z, world, stack, shotFrom);
        setDamage(2.5);
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return new ItemStack(ModItems.IRON_ARROW);
    }
}
