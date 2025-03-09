package toady.fletching.item.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
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
import toady.fletching.entity.custom.SlimeArrowEntity;

import java.util.List;

public class SlimeArrowItem extends ArrowItem {
    public SlimeArrowItem(Item.Settings settings) {
        super(settings);
    }

    @Override
    public PersistentProjectileEntity createArrow(World world, ItemStack stack, LivingEntity shooter, @Nullable ItemStack shotFrom) {
        return new SlimeArrowEntity(world, shooter, stack.copyWithCount(1), shotFrom);
    }

    @Override
    public ProjectileEntity createEntity(World world, Position pos, ItemStack stack, Direction direction) {
        SlimeArrowEntity arrowEntity = new SlimeArrowEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack.copyWithCount(1), null);
        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.ALLOWED;
        return arrowEntity;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (Screen.hasShiftDown()){
            tooltip.add(Text.literal("§8Bounces off blocks, retaining most momentum."));
        }
        else {
            tooltip.add(Text.literal("§8Press [§7Sneak§8] for Summary"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
}
