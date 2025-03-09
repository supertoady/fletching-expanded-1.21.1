package toady.fletching.network;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import toady.fletching.FletchingExpanded;

public record UpdateQuiverComponentsPayload(int slot, ItemStack quiverStack) implements CustomPayload {
    public static final Identifier IDENTIFIER = FletchingExpanded.id("update_quiver_components");
    public static final CustomPayload.Id<UpdateQuiverComponentsPayload> ID = new CustomPayload.Id<>(IDENTIFIER);
    public static final PacketCodec<RegistryByteBuf, UpdateQuiverComponentsPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.INTEGER, UpdateQuiverComponentsPayload::slot,
            ItemStack.PACKET_CODEC, UpdateQuiverComponentsPayload::quiverStack,
            UpdateQuiverComponentsPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}
