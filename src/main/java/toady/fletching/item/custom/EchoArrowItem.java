package toady.fletching.item.custom;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.LivingEntity;
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
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.custom.EchoArrowEntity;

import java.util.List;

public class EchoArrowItem extends ArrowItem {
    public EchoArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new EchoArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        EchoArrowEntity arrowEntity = new EchoArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            if (FletchingExpanded.hasShiftDown()){
                tooltip.add(Text.literal("§8Creates a warden explosion on impact."));
                tooltip.add(Text.literal("§8The explosion deals no damage but has"));
                tooltip.add(Text.literal("§8massive knockback."));
            }
            else {
                tooltip.add(Text.literal("§8Press [§7Sneak§8] for Summary"));
            }
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
