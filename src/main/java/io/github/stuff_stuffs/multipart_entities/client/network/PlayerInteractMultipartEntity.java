package io.github.stuff_stuffs.multipart_entities.client.network;

import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class PlayerInteractMultipartEntity {
    public static final Identifier IDENTIFIER = new Identifier("multipart_entities", "interact");

    public static void attack(final MinecraftClient client, final Hand hand, final MultipartAwareEntity entity) {
        final Vec3d pos = client.cameraEntity.getCameraPosVec(client.getTickDelta());
        final Vec3d dir = client.cameraEntity.getRotationVec(client.getTickDelta());
        final double reach = client.interactionManager.getReachDistance();
        final String part = entity.getBounds().raycast(pos, pos.add(dir.multiply(reach)));
        if (part != null) {
            final PacketByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(((Entity) entity).getEntityId());
            buf.writeString(part);
            buf.writeEnumConstant(InteractionType.ATTACK);
            buf.writeEnumConstant(hand);
            buf.writeBoolean(client.cameraEntity.isSneaking());
            ClientPlayNetworking.send(IDENTIFIER, buf);
        }
    }

    public static ActionResult interact(final MinecraftClient client, final Hand hand, final MultipartAwareEntity entity) {
        final Vec3d pos = client.cameraEntity.getCameraPosVec(client.getTickDelta());
        final Vec3d dir = client.cameraEntity.getRotationVec(client.getTickDelta());
        final double reach = client.interactionManager.getReachDistance();
        final String part = entity.getBounds().raycast(pos, pos.add(dir.multiply(reach)));
        if (part != null) {
            final PacketByteBuf buf = PacketByteBufs.create();
            buf.writeVarInt(((Entity) entity).getEntityId());
            buf.writeString(part);
            buf.writeEnumConstant(InteractionType.INTERACT);
            buf.writeEnumConstant(hand);
            buf.writeBoolean(client.cameraEntity.isSneaking());
            ClientPlayNetworking.send(IDENTIFIER, buf);
            return entity.interact(client.cameraEntity, hand, part);
        }
        return ActionResult.PASS;
    }

    public enum InteractionType {
        ATTACK,
        INTERACT
    }
}
