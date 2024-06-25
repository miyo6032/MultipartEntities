package io.github.stuff_stuffs.multipart_entities.common.network;

import io.github.stuff_stuffs.multipart_entities.client.network.PlayerInteractMultipartEntity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public record InteractPacket(int id, String part, PlayerInteractMultipartEntity.InteractionType interactionType, Hand hand, boolean isSneaking) implements CustomPayload {
    public static final CustomPayload.Id<InteractPacket> PACKET_ID = new CustomPayload.Id<>(Identifier.of("multipart_entities", "interact"));
    public static final PacketCodec<RegistryByteBuf, InteractPacket> PACKET_CODEC = PacketCodec.ofStatic(InteractPacket::write2, InteractPacket::new);

    public static void write2(RegistryByteBuf buf, InteractPacket packet) {
        buf.writeVarInt(packet.id);
        buf.writeString(packet.part);
        buf.writeEnumConstant(packet.interactionType);
        buf.writeEnumConstant(packet.hand);
        buf.writeBoolean(packet.isSneaking);
    }
    
    public InteractPacket(RegistryByteBuf buf) {
        this(buf.readVarInt(), buf.readString(32767), buf.readEnumConstant(PlayerInteractMultipartEntity.InteractionType.class), buf.readEnumConstant(Hand.class), buf.readBoolean());
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}
