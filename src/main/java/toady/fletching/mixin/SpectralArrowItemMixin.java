package toady.fletching.mixin;

import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpectralArrowItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import toady.fletching.FletchingExpanded;

import java.util.List;

@Mixin(SpectralArrowItem.class)
public abstract class SpectralArrowItemMixin extends ArrowItem {
    public SpectralArrowItemMixin(Item.Settings settings) {
        super(settings);
    }

    @Unique
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if (FletchingExpanded.hasShiftDown()){
            tooltip.add(Text.literal("§8Illuminates enemies on impact, allowing"));
            tooltip.add(Text.literal("§8you to see their outline through blocks."));
        }
        else {
            tooltip.add(Text.literal("§8Press [§7Sneak§8] for Summary"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

}
