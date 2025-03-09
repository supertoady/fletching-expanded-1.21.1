package toady.fletching.item;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import toady.fletching.FletchingExpanded;

public class ModComponents {
    public static final ComponentType<ItemStack> QUIVER_MAIN_STACK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FletchingExpanded.MOD_ID, "quiver_main_stack"),
            ComponentType.<ItemStack>builder().codec(ItemStack.CODEC).build()
    );

    public static final ComponentType<ItemStack> QUIVER_SECONDARY_STACK = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(FletchingExpanded.MOD_ID, "quiver_secondary_stack"),
            ComponentType.<ItemStack>builder().codec(ItemStack.CODEC).build()
    );

    public static void initialize() {}
}
