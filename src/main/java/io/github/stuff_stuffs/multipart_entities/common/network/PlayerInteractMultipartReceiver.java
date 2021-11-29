package io.github.stuff_stuffs.multipart_entities.common.network;

import io.github.stuff_stuffs.multipart_entities.client.network.PlayerInteractMultipartEntity;
import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class PlayerInteractMultipartReceiver {

    private static void receive(final MinecraftServer minecraftServer, final ServerPlayerEntity serverPlayerEntity, final ServerPlayNetworkHandler serverPlayNetworkHandler, final PacketByteBuf packetByteBuf, final PacketSender packetSender) {
        final int entityId = packetByteBuf.readVarInt();
        final String part = packetByteBuf.readString(32767);
        final PlayerInteractMultipartEntity.InteractionType interactionType = packetByteBuf.readEnumConstant(PlayerInteractMultipartEntity.InteractionType.class);
        final Hand hand = packetByteBuf.readEnumConstant(Hand.class);
        final boolean isSneaking = packetByteBuf.readBoolean();
        minecraftServer.execute(() -> {
            serverPlayerEntity.setSneaking(isSneaking);
            final ServerWorld world = serverPlayerEntity.getWorld();
            final Entity entity = world.getEntityById(entityId);
            assert entity != null;
            if (interactionType == PlayerInteractMultipartEntity.InteractionType.INTERACT) {
                entity.interact(serverPlayerEntity, hand);
            } else if (interactionType == PlayerInteractMultipartEntity.InteractionType.ATTACK) {
                if (entity instanceof MultipartAwareEntity) {
                    ((MultipartAwareEntity) entity).setNextDamagedPart(part);
                }
                serverPlayerEntity.attack(entity);
            } else {
                throw new RuntimeException();
            }
        });
    }

    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(PlayerInteractMultipartEntity.IDENTIFIER, PlayerInteractMultipartReceiver::receive);
    }
}
