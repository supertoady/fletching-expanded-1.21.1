package toady.fletching.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import toady.fletching.FletchingExpanded;

public class ModStatusEffects {
    public static final RegistryEntry<StatusEffect> STREAK = register("streak", new StreakStatusEffect(StatusEffectCategory.BENEFICIAL, 0x80E0FF)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, FletchingExpanded.id("effect.streak"), 0.15, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static RegistryEntry<StatusEffect> register(String id, StatusEffect effect){
        return Registry.registerReference(Registries.STATUS_EFFECT, FletchingExpanded.id(id), effect);
    }

    public static void initialize() {

    }
}
