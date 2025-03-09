package toady.fletching.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import toady.fletching.FletchingExpanded;
import toady.fletching.entity.custom.*;

public class ModEntityType {
    public static final EntityType<IronArrowEntity> IRON_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("iron_arrow"),
            EntityType.Builder.<IronArrowEntity>create(IronArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("iron_arrow")
    );

    public static final EntityType<AmethystArrowEntity> AMETHYST_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("amethyst_arrow"),
            EntityType.Builder.<AmethystArrowEntity>create(AmethystArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("amethyst_arrow")
    );

    public static final EntityType<EchoArrowEntity> ECHO_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("echo_arrow"),
            EntityType.Builder.<EchoArrowEntity>create(EchoArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("echo_arrow")
    );

    public static final EntityType<PhantomArrowEntity> PHANTOM_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("phantom_arrow"),
            EntityType.Builder.<PhantomArrowEntity>create(PhantomArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("phantom_arrow")
    );

    public static final EntityType<HomingArrowEntity> HOMING_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("homing_arrow"),
            EntityType.Builder.<HomingArrowEntity>create(HomingArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("homing_arrow")
    );

    public static final EntityType<SlimeArrowEntity> SLIME_ARROW = Registry.register(
            Registries.ENTITY_TYPE,
            FletchingExpanded.id("slime_arrow"),
            EntityType.Builder.<SlimeArrowEntity>create(SlimeArrowEntity::new, SpawnGroup.CREATURE).dimensions(0.5F, 0.5F).build("slime_arrow")
    );

    public static void initialize() {}
}
