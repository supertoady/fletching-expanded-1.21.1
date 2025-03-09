package toady.fletching.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import toady.fletching.FletchingExpanded;

public class ModSounds {
    public static SoundEvent ENTITY_HOMING_ARROW_TARGET = registerSound("entity.homing_arrow.target");
    public static RegistryEntry.Reference<SoundEvent> ENTITY_ECHO_ARROW_HIT = registerReference("entity.echo_arrow.hit");
    public static SoundEvent ENTITY_SLIME_ARROW_HIT = registerSound("entity.slime_arrow.hit");
    public static SoundEvent BLOCK_FLETCHING_TABLE_POTION_CRAFT = registerSound("block.fletching_table.potion_craft");
    //public static SoundEvent ENCHANT_OVERCHARGED_FULL_CHARGE = registerSound("enchant.overcharged.full_charge");

    private static SoundEvent registerSound(String name){
        Identifier id = FletchingExpanded.id(name);
        Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
        return SoundEvent.of(id);
    }

    private static RegistryEntry.Reference<SoundEvent> registerReference(String name){
        Identifier id = FletchingExpanded.id(name);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {}
}
