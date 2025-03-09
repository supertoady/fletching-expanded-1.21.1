package toady.fletching;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.text.Text;
import toady.fletching.entity.ModEntityType;
import toady.fletching.entity.custom.renderer.*;
import toady.fletching.gui.FletchingScreen;
import toady.fletching.item.ModItems;
import toady.fletching.item.custom.QuiverItem;
import toady.fletching.network.UpdateQuiverComponentsPayload;

public class FletchingExpandedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelPredicateProviderRegistry.register(ModItems.QUIVER, FletchingExpanded.id("loaded"), (itemStack, clientWorld, livingEntity, seed) ->
                !QuiverItem.getMainStack(itemStack).isEmpty() ? 1.0F : 0.0F);

        //screen stuff VVV
        HandledScreens.register(FletchingExpanded.FLETCHING_HANDLER, FletchingScreen::new);

        //renderer stuff VVV
        EntityRendererRegistry.register(ModEntityType.IRON_ARROW,IronArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.AMETHYST_ARROW, AmethystArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.ECHO_ARROW, EchoArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.PHANTOM_ARROW, PhantomArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.SLIME_ARROW, SlimeArrowEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityType.HOMING_ARROW, HomingArrowEntityRenderer::new);

        //packet receiver
        ClientPlayNetworking.registerGlobalReceiver(UpdateQuiverComponentsPayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                context.player().getInventory().setStack(payload.slot(), payload.quiverStack());
            });
        });
    }
}
