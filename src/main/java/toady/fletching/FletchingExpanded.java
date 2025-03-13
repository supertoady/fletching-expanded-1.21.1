package toady.fletching;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import toady.fletching.effect.ModStatusEffects;
import toady.fletching.enchant.ModEnchantments;
import toady.fletching.entity.ModEntityType;
import toady.fletching.gui.FletchingScreenHandler;
import toady.fletching.item.ModItems;
import toady.fletching.item.ModComponents;
import toady.fletching.item.custom.QuiverItem;
import toady.fletching.network.UpdateQuiverComponentsPayload;
import toady.fletching.sound.ModSounds;

import java.util.Optional;
import java.util.Random;

public class FletchingExpanded implements ModInitializer {

	public static final String MOD_ID = "fletching-expanded";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final ScreenHandlerType<FletchingScreenHandler> FLETCHING_HANDLER =
			Registry.register(Registries.SCREEN_HANDLER, id("fletching_handler"),
					new ScreenHandlerType<>(FletchingScreenHandler::new, FeatureSet.empty()));

	public static final GameRules.Key<GameRules.IntRule> EXPLOSIVE_POWER =
			GameRuleRegistry.register("fletching.explosiveArrowPower",
					GameRules.Category.PLAYER, GameRuleFactory.createIntRule(1, 0, 100));

	public static final GameRules.Key<GameRules.IntRule> STREAK_DURATION =
			GameRuleRegistry.register("fletching.streakEffectDuration",
					GameRules.Category.PLAYER, GameRuleFactory.createIntRule(5, 1, 1000));

	public static final GameRules.Key<GameRules.IntRule> SLIME_BOUNCES =
			GameRuleRegistry.register("fletching.slimeArrowBounces",
					GameRules.Category.PLAYER, GameRuleFactory.createIntRule(3, 1, 1000));

	public static final GameRules.Key<GameRules.IntRule> HOMING_RANGE =
			GameRuleRegistry.register("fletching.homingArrowRange",
					GameRules.Category.PLAYER, GameRuleFactory.createIntRule(2, 1, 10));

	@Override
	public void onInitialize() {

		//add arrows to item group
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
			content.add(ModItems.IRON_ARROW);
			content.add(ModItems.FROST_ARROW);
			content.add(ModItems.FLAME_ARROW);
			content.add(ModItems.AMETHYST_ARROW);
			content.add(ModItems.ECHO_ARROW);
			content.add(ModItems.SLIME_ARROW);
			content.add(ModItems.PHANTOM_ARROW);
			content.add(ModItems.HOMING_ARROW);
			content.add(ModItems.QUIVER);
		});
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
			content.add(ModItems.ARROW_SHAFT);
		});

		//register packets
		PayloadTypeRegistry.playS2C().register(UpdateQuiverComponentsPayload.ID, UpdateQuiverComponentsPayload.CODEC);

		//kinda obvious
		registerVillagerTrades();

		//initialize all content registries
		ModItems.initialize();
		ModItems.registerDispenserBehaviour();
		ModStatusEffects.initialize();
		ModSounds.initialize();
		ModComponents.initialize();
		ModEnchantments.initialize();
		ModEntityType.initialize();
		LOGGER.info("Fletching Expanded loaded!");
	}

	//random util methods
	public static void showParticlesToAll(World world, ParticleEffect particle, Vec3d pos, double delta, int count, double speed){
		if (world.getServer() == null) return;
		ServerWorld serverWorld = world.getServer().getWorld(world.getRegistryKey());
		if (serverWorld != null){
			serverWorld.spawnParticles(particle, pos.x, pos.y, pos.z, count, delta, delta, delta, speed);
		}
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public void registerVillagerTrades(){
		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 1, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 3),
					new ItemStack(ModItems.ARROW_SHAFT, 64), 12, 1, 0.05f
			));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 2, factories -> {
			ItemStack quiver = new ItemStack(ModItems.QUIVER);
			int count = new Random().nextInt(16, 40);
			quiver.set(ModComponents.QUIVER_MAIN_STACK, new ItemStack(Items.ARROW, 64));
			quiver.set(ModComponents.QUIVER_SECONDARY_STACK, new ItemStack(Items.ARROW, count));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 12),
					Optional.of(new TradedItem(Items.LEATHER, 2)),
					quiver, 3, 4, 0.05f
			));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(Items.EMERALD, 2),
					new ItemStack(ModItems.IRON_ARROW, 16), 10, 1, 0.05f
			));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 3, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.ARROW_SHAFT, 16),
					Optional.of(new TradedItem(Items.EMERALD, 3)),
					new ItemStack(ModItems.FLAME_ARROW, 16), 8, 2, 0.05f
			));
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.ARROW_SHAFT, 16),
					Optional.of(new TradedItem(Items.EMERALD, 3)),
					new ItemStack(ModItems.FROST_ARROW, 16), 8, 2, 0.05f
			));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 4, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.ARROW_SHAFT, 12),
					Optional.of(new TradedItem(Items.EMERALD, 4)),
					new ItemStack(ModItems.HOMING_ARROW, 12), 8, 2, 0.05f
			));

			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.ARROW_SHAFT, 16),
					Optional.of(new TradedItem(Items.EMERALD, 2)),
					new ItemStack(ModItems.PHANTOM_ARROW, 16), 8, 2, 0.05f
			));
		});

		TradeOfferHelper.registerVillagerOffers(VillagerProfession.FLETCHER, 5, factories -> {
			factories.add((entity, random) -> new TradeOffer(
					new TradedItem(ModItems.ARROW_SHAFT, 16),
					Optional.of(new TradedItem(Items.EMERALD, 3)),
					new ItemStack(ModItems.AMETHYST_ARROW, 16), 8, 2, 0.05f
			));
		});
	}
}