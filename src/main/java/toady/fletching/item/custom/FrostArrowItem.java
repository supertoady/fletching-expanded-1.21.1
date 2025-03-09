package toady.fletching.item.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FrostArrowItem extends ArrowItem {
    public FrostArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        PersistentProjectileEntity persistentProjectileEntity = super.createArrow(world, stack.copyWithCount(1), shooter, shotFrom);
        persistentProjectileEntity.setFrozenTicks(1200);
        return persistentProjectileEntity;
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        ArrowEntity arrowEntity = new ArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        arrowEntity.setFrozenTicks(1200);
        return arrowEntity;
    }
}
