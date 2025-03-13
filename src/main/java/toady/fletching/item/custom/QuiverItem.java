package toady.fletching.item.custom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.world.World;
import toady.fletching.FletchingExpanded;
import toady.fletching.item.ModComponents;

import java.util.List;

public class QuiverItem extends Item {
    public QuiverItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack quiver = user.getStackInHand(hand);
        ItemStack mainStack = getMainStack(quiver);
        ItemStack secondaryStack = getSecondaryStack(quiver);
        if (!world.isClient && !mainStack.isEmpty() && !secondaryStack.isEmpty() && !mainStack.isOf(secondaryStack.getItem())){
            user.sendMessage(Text.literal("Current Projectile §7- ").append(Text.translatable(secondaryStack.getTranslationKey())).append(" (" + secondaryStack.getCount() + ")"), true);
            user.playSoundToPlayer(SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1, 1);

            quiver.set(ModComponents.QUIVER_MAIN_STACK, secondaryStack);
            quiver.set(ModComponents.QUIVER_SECONDARY_STACK, mainStack);
        }

        return super.use(world, user, hand);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT){
            if (otherStack.isEmpty()){
                if (!getMainStack(stack).isEmpty()){
                    cursorStackReference.set(getMainStack(stack));
                    stack.set(ModComponents.QUIVER_MAIN_STACK, getSecondaryStack(stack));
                    stack.set(ModComponents.QUIVER_SECONDARY_STACK, ItemStack.EMPTY);
                    player.playSoundToPlayer(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, SoundCategory.PLAYERS, 1, 1);
                    return true;
                }
            }
            else if (otherStack.isIn(ItemTags.ARROWS)){
                addProjectile(stack, otherStack);
                player.playSoundToPlayer(SoundEvents.ITEM_BUNDLE_INSERT, SoundCategory.PLAYERS, 1, 1);
                return true;
            }
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        ItemStack mainStack = getMainStack(stack);
        ItemStack secondaryStack = getSecondaryStack(stack);

        if (!mainStack.isEmpty()) {
            tooltip.add(Text.literal("§7- §r").append(Text.translatable(mainStack.getTranslationKey())).append(Text.literal(" (" + mainStack.getCount() + ")")));
        }
        if (!secondaryStack.isEmpty()) {
            tooltip.add(Text.literal("§7- §r").append(Text.translatable(secondaryStack.getTranslationKey())).append(Text.literal(" (" + secondaryStack.getCount() + ")")));
        }
        if (!(mainStack.isEmpty() && secondaryStack.isEmpty())){
            tooltip.add(Text.literal(" "));
        }

        if (Screen.hasShiftDown()){
            tooltip.add(Text.literal("§8Allows you to store two stacks of any arrow type."));
            tooltip.add(Text.literal("§8Automatically picks up arrows and inserts them into"));
            tooltip.add(Text.literal("§8the quiver."));
            tooltip.add(Text.literal(" "));
            tooltip.add(Text.literal("§7On Right Click:"));
            tooltip.add(Text.literal("§8Switches the main and secondary stacks."));
        }
        else {
            tooltip.add(Text.literal("§8Press [§7Sneak§8] for Summary"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int totalCount = getMainStack(stack).getCount() + getSecondaryStack(stack).getCount();
        float step = ((float) totalCount / 128);
        return (int) Math.ceil(step * 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ColorHelper.Argb.fromFloats(1.0F, 0.44F, 0.53F, 1.0F);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return !getMainStack(stack).isEmpty();
    }

    //utility functions ↓↓↓
    public static ItemStack getMainStack(ItemStack quiver){
        if (quiver.get(ModComponents.QUIVER_MAIN_STACK) == null) return ItemStack.EMPTY;
        return quiver.get(ModComponents.QUIVER_MAIN_STACK);
    }

    public static ItemStack getSecondaryStack(ItemStack quiver){
        if (quiver.get(ModComponents.QUIVER_SECONDARY_STACK) == null) return ItemStack.EMPTY;
        return quiver.get(ModComponents.QUIVER_SECONDARY_STACK);
    }

    public static void addProjectile(ItemStack quiver, ItemStack projectile){
        ItemStack mainStack = getMainStack(quiver);
        ItemStack secondaryStack = getSecondaryStack(quiver);
        if (mainStack.isOf(projectile.getItem()) && mainStack.getCount() < mainStack.getMaxCount()){
            int difference = mainStack.getMaxCount() - mainStack.getCount();
            int resultCount = Math.min(mainStack.getCount() + projectile.getCount(), 64);
            mainStack.setCount(resultCount);
            quiver.set(ModComponents.QUIVER_MAIN_STACK, mainStack);
            projectile.decrement(difference);
        }
        if (secondaryStack.isOf(projectile.getItem()) && secondaryStack.getCount() < secondaryStack.getMaxCount() && !projectile.isEmpty()){
            int difference = secondaryStack.getMaxCount() - secondaryStack.getCount();
            int resultCount = Math.min(secondaryStack.getCount() + projectile.getCount(), 64);
            secondaryStack.setCount(resultCount);
            quiver.set(ModComponents.QUIVER_SECONDARY_STACK, secondaryStack);
            projectile.decrement(difference);
        }

        if (!projectile.isEmpty()){
            ItemStack newProjectile = projectile.copy();
            if (mainStack.isEmpty()){
                projectile.decrement(projectile.getCount());
                quiver.set(ModComponents.QUIVER_MAIN_STACK, newProjectile);
            }
            else if (secondaryStack.isEmpty()) {
                projectile.decrement(projectile.getCount());
                quiver.set(ModComponents.QUIVER_SECONDARY_STACK, newProjectile);
            }
        }
    }

    public static ItemStack removeNextProjectile(LivingEntity shooter, ItemStack quiver){
        ItemStack mainStack = getMainStack(quiver);
        ItemStack secondaryStack = getSecondaryStack(quiver);
        ItemStack returnStack = mainStack.copyWithCount(1);

        if (mainStack.isOf(secondaryStack.getItem())) {
            if (secondaryStack.getCount() == 1) secondaryStack = ItemStack.EMPTY;
            else secondaryStack.decrement(1);
            quiver.set(ModComponents.QUIVER_SECONDARY_STACK, secondaryStack);
        } else {
            if (mainStack.getCount() == 1) mainStack = ItemStack.EMPTY;
            else mainStack.decrement(1);
            quiver.set(ModComponents.QUIVER_MAIN_STACK, mainStack);
            if (mainStack.isEmpty() && !getSecondaryStack(quiver).isEmpty()) {
                quiver.set(ModComponents.QUIVER_MAIN_STACK, getSecondaryStack(quiver));
                quiver.set(ModComponents.QUIVER_SECONDARY_STACK, ItemStack.EMPTY);
            }
        }

        return returnStack;
    }
}
