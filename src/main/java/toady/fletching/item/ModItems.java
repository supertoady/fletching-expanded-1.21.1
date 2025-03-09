package toady.fletching.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import toady.fletching.FletchingExpanded;
import toady.fletching.item.custom.*;

public class ModItems {

    public static final Item ARROW_SHAFT = registerItem("arrow_shaft",
            new Item(new Item.Settings()));

    public static final Item IRON_ARROW = registerItem("iron_arrow",
            new IronArrowItem(new Item.Settings()));

    public static final Item FLAME_ARROW = registerItem("flame_arrow",
            new FlameArrowItem(new Item.Settings()));

    public static final Item FROST_ARROW = registerItem("frost_arrow",
            new FrostArrowItem(new Item.Settings()));

    public static final Item AMETHYST_ARROW = registerItem("amethyst_arrow",
            new AmethystArrowItem(new Item.Settings()));

    public static final Item ECHO_ARROW = registerItem("echo_arrow",
            new EchoArrowItem(new Item.Settings()));

    public static final Item SLIME_ARROW = registerItem("slime_arrow",
            new SlimeArrowItem(new Item.Settings()));

    public static final Item PHANTOM_ARROW = registerItem("phantom_arrow",
            new PhantomArrowItem(new Item.Settings()));

    public static final Item HOMING_ARROW = registerItem("homing_arrow",
            new HomingArrowItem(new Item.Settings()));

    public static final Item QUIVER = registerItem("quiver",
            new QuiverItem(new Item.Settings().maxCount(1).component(ModComponents.QUIVER_MAIN_STACK, ItemStack.EMPTY)
                    .component(ModComponents.QUIVER_SECONDARY_STACK, ItemStack.EMPTY)));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, FletchingExpanded.id(name), item);
    }

    public static void registerDispenserBehaviour(){
        DispenserBlock.registerProjectileBehavior(ModItems.IRON_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.FLAME_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.FROST_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.AMETHYST_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.ECHO_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.SLIME_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.PHANTOM_ARROW);
        DispenserBlock.registerProjectileBehavior(ModItems.HOMING_ARROW);
    }

    public static void initialize() {}
}
