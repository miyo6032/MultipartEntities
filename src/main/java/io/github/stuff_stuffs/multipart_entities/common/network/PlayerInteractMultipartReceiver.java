package io.github.stuff_stuffs.multipart_entities.common.network;

import io.github.stuff_stuffs.multipart_entities.client.network.PlayerInteractMultipartEntity;
import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public class PlayerInteractMultipartReceiver {

    private static void receive(final MinecraftServer minecraftServer, final ServerPlayerEntity serverPlayerEntity, final InteractPacket interactPacket) {
        final int entityId = interactPacket.id();
        final String part = interactPacket.part();
        final PlayerInteractMultipartEntity.InteractionType interactionType = interactPacket.interactionType();
        final Hand hand = interactPacket.hand();
        final boolean isSneaking = interactPacket.isSneaking();
        minecraftServer.execute(() -> {
            serverPlayerEntity.setSneaking(isSneaking);
            final ServerWorld world = serverPlayerEntity.getServerWorld();
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
        PayloadTypeRegistry.playC2S().register(InteractPacket.PACKET_ID, InteractPacket.PACKET_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(InteractPacket.PACKET_ID, (payload, context) -> {
            PlayerInteractMultipartReceiver.receive(context.player().server, context.player(), payload);
        });
    }
}
