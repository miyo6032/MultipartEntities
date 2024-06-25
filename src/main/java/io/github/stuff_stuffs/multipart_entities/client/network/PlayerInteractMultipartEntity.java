package io.github.stuff_stuffs.multipart_entities.client.network;

import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import io.github.stuff_stuffs.multipart_entities.common.network.InteractPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class PlayerInteractMultipartEntity {

    public static void attack(final MinecraftClient client, final Hand hand, final MultipartAwareEntity entity) {
        final Vec3d pos = client.cameraEntity.getCameraPosVec(client.getRenderTickCounter().getTickDelta(true));
        final Vec3d dir = client.cameraEntity.getRotationVec(client.getRenderTickCounter().getTickDelta(true));
        final double reach = client.player.getEntityInteractionRange();
        final String part = entity.getBounds().raycast(pos, pos.add(dir.multiply(reach)));
        if (part != null) {
            var id = ((Entity) entity).getId();
            var interactionType = InteractionType.ATTACK;
            var isSneaking = client.cameraEntity.isSneaking();
            ClientPlayNetworking.send(new InteractPacket(id, part, interactionType, hand, isSneaking));
        }
    }

    public static ActionResult interact(final MinecraftClient client, final Hand hand, final MultipartAwareEntity entity) {
        final Vec3d pos = client.cameraEntity.getCameraPosVec(client.getRenderTickCounter().getTickDelta(true));
        final Vec3d dir = client.cameraEntity.getRotationVec(client.getRenderTickCounter().getTickDelta(true));
        final double reach = client.player.getEntityInteractionRange();
        final String part = entity.getBounds().raycast(pos, pos.add(dir.multiply(reach)));
        if (part != null) {
            var id = ((Entity) entity).getId();
            var interactionType = InteractionType.INTERACT;
            var isSneaking = client.cameraEntity.isSneaking();
            ClientPlayNetworking.send(new InteractPacket(id, part, interactionType, hand, isSneaking));
            return entity.interact(client.cameraEntity, hand, part);
        }
        return ActionResult.PASS;
    }

    public enum InteractionType {
        ATTACK,
        INTERACT
    }
}
