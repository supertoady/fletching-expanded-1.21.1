package toady.fletching.enchant;

import com.mojang.serialization.MapCodec;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import toady.fletching.FletchingExpanded;

public class ModEnchantments {
    public static MapCodec<ExplosiveEnchantmentEffect> EXPLOSIVE_EFFECT = register("explosive_effect", ExplosiveEnchantmentEffect.CODEC);
    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, FletchingExpanded.id(id), codec);
    }

    public static void initialize() {}
}
