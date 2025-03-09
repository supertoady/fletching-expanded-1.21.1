package toady.fletching.item.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import toady.fletching.entity.custom.EchoArrowEntity;
import toady.fletching.entity.custom.IronArrowEntity;

public class IronArrowItem extends ArrowItem {
    public IronArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new IronArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        IronArrowEntity arrowEntity = new IronArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }
}
