package io.github.stuff_stuffs.multipart_entities.mixin.client;

import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartEntity;
import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import io.github.stuff_stuffs.multipart_entities.common.util.OrientedBox;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Inject(method = "renderHitbox", at = @At("RETURN"))
    private static void drawOrientedBoxes(MatrixStack matrix, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo ci) {
        if (entity instanceof MultipartEntity) {
            final CompoundOrientedBox box = ((MultipartEntity) entity).getCompoundBoundingBox();
            matrix.push();
            matrix.translate(-entity.getX(), -entity.getY(), -entity.getZ());
            for (final OrientedBox orientedBox : box) {
                matrix.push();
                final Vec3d center = orientedBox.getCenter();
                matrix.translate(center.x, center.y, center.z);
                matrix.multiply(orientedBox.getRotation().toFloatQuat());
                WorldRenderer.drawBox(matrix, vertices, orientedBox.getExtents(), 0, 0, 1, 1);
                matrix.pop();
            }
            box.toVoxelShape().forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> WorldRenderer.drawBox(matrix, vertices, minX, minY, minZ, maxX, maxY, maxZ, 0, 1, 0, 1f));
            matrix.pop();
        }
    }
}
