package toady.fletching.gui;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import toady.fletching.FletchingExpanded;
import toady.fletching.item.ModItems;
import toady.fletching.sound.ModSounds;

import java.util.List;

public class FletchingScreenHandler extends ScreenHandler {
    protected final ScreenHandlerContext context;
    protected final Inventory input;
    protected final CraftingResultInventory output = new CraftingResultInventory();

    public static final List<List<Item>> recipes = List.of(
            List.of(ModItems.ARROW_SHAFT, Items.FLINT, Items.ARROW),
            List.of(ModItems.ARROW_SHAFT, Items.IRON_INGOT, ModItems.IRON_ARROW),
            List.of(Items.ARROW, Items.GLOWSTONE_DUST, Items.SPECTRAL_ARROW),
            List.of(Items.ARROW, Items.FIRE_CHARGE, ModItems.FLAME_ARROW),
            List.of(Items.ARROW, Items.BLUE_ICE, ModItems.FROST_ARROW),
            List.of(ModItems.ARROW_SHAFT, Items.AMETHYST_SHARD, ModItems.AMETHYST_ARROW),
            List.of(ModItems.ARROW_SHAFT, Items.ECHO_SHARD, ModItems.ECHO_ARROW),
            List.of(ModItems.ARROW_SHAFT, Items.ENDER_EYE, ModItems.HOMING_ARROW),
            List.of(ModItems.ARROW_SHAFT, Items.SLIME_BALL, ModItems.SLIME_ARROW),
            List.of(Items.ARROW, Items.PHANTOM_MEMBRANE, ModItems.PHANTOM_ARROW)
            );

    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    // This constructor gets called from the BlockEntity on the server without calling the other constructor first, the server knows the inventory of the container
    // and can therefore directly provide it as an argument. This inventory will then be synced to the client.
    public FletchingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(FletchingExpanded.FLETCHING_HANDLER, syncId);
        this.context = context;
        this.input = createInputInventory();

        // This will place the slot in the correct locations for a 3x3 Grid. The slots exist on both server and client!
        // This will not render the background of the slots however, this is the Screens job
        int m;
        int l;
        // Our inventory
        this.addSlot(new Slot(input, 0, 48, 52));
        this.addSlot(new Slot(input, 1, 48, 20));
        this.addSlot(new Slot(output, 0, 112, 35) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return FletchingScreenHandler.this.canTakeOutput(playerEntity, this.hasStack());
            }

            public void onTakeItem(PlayerEntity player, ItemStack stack) {
                FletchingScreenHandler.this.onTakeOutput(player, stack);
            }
        });

        // The player inventory
        for (m = 0; m < 3; ++m) {
            for (l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + m * 9 + 9, 8 + l * 18, 84 + m * 18));
            }
        }
        // The player Hotbar
        for (m = 0; m < 9; ++m) {
            this.addSlot(new Slot(playerInventory, m, 8 + m * 18, 142));
        }

    }

    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return true;
    }

    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        //player.sendMessage(Text.of("you just took output. you crafted " + stack.getItem() + ". " + player.getWorld().isClient));
        stack.onCraftByPlayer(player.getWorld(), player, stack.getCount());
        if (!player.getWorld().isClient && input.getStack(1).isOf(Items.POTION)){
            player.playSoundToPlayer(ModSounds.BLOCK_FLETCHING_TABLE_POTION_CRAFT, SoundCategory.BLOCKS, 1, 1);
        }
        else {
            player.playSoundToPlayer(ModSounds.BLOCK_FLETCHING_TABLE_CRAFT, SoundCategory.BLOCKS, 1, 1);
        }
        input.getStack(0).decrement(8);
        input.getStack(1).decrement(1);
        this.onContentChanged(input);
    }

    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        updateResult();
    }

    public void updateResult() {
        ItemStack arrow = input.getStack(0);
        ItemStack tip = input.getStack(1);
        ItemStack result = ItemStack.EMPTY;
        if (tip.isOf(Items.POTION)){
            if (arrow.isOf(Items.ARROW) && arrow.getCount() >= 8){
                result = new ItemStack(Items.TIPPED_ARROW, 8);
                result.set(DataComponentTypes.POTION_CONTENTS, tip.get(DataComponentTypes.POTION_CONTENTS));
            }
        }

        for (List<Item> list : recipes){
            if (list.size() > 2){
                if (arrow.isOf(list.getFirst()) && arrow.getCount() >= 8){
                    if (tip.isOf(list.get(1))){
                        result = new ItemStack(list.get(2), 8);
                        break;
                    }
                }
            }
        }
        output.setStack(0, result);
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
        this.dropInventory(player, this.input);
    }

    private SimpleInventory createInputInventory() {
        return new SimpleInventory(2) {
            public void markDirty() {
                super.markDirty();
                onContentChanged(this);
            }
        };
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.context.get((world, pos) -> this.canUse(world.getBlockState(pos)) &&
                player.canInteractWithBlockAt(pos, 4.0), true);
    }

    protected boolean canUse(BlockState state) {
        return state.isOf(Blocks.SMITHING_TABLE);
    }

    // Shift + Player Inv Slot
    @Override
    public ItemStack quickMove(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.input.size()) {
                if (!this.insertItem(originalStack, this.input.size(), this.slots.size(), true)) {
                    FletchingExpanded.LOGGER.info("yes inv < input");
                    return ItemStack.EMPTY;
                }
            } else if (invSlot == 2) {
                originalStack.onCraftByPlayer(player.getWorld(), player, originalStack.getCount());
                if (!player.getWorld().isClient && input.getStack(1).isOf(Items.POTION)){
                    player.playSoundToPlayer(ModSounds.BLOCK_FLETCHING_TABLE_POTION_CRAFT, SoundCategory.BLOCKS, 1, 1);
                }

                ItemStack shaft = input.getStack(0);
                ItemStack tip = input.getStack(1);
                int craftAmount = (int) Math.floor((double) shaft.getCount() / 8);
                craftAmount = Math.min(craftAmount, tip.getCount());
                FletchingExpanded.LOGGER.info("test: " + shaft + ", " + tip + ", " + craftAmount);

                shaft.decrement(craftAmount * 8);
                tip.decrement(craftAmount);
                originalStack.setCount(craftAmount * 8);
                onContentChanged(input);
                if (!this.insertItem(originalStack, this.output.size(), this.slots.size(), true)) {
                    FletchingExpanded.LOGGER.info("yes");
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.input.size(), false)) {
                FletchingExpanded.LOGGER.info("yes else if");
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (invSlot != 2) {
                slot.onTakeItem(player, originalStack);
            }
        }

        return newStack;
    }
}