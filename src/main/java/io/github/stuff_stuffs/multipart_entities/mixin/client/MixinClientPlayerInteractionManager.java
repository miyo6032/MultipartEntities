package io.github.stuff_stuffs.multipart_entities.mixin.client;

import io.github.stuff_stuffs.multipart_entities.client.network.PlayerInteractMultipartEntity;
import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    protected abstract void syncSelectedSlot();

    @Shadow
    private GameMode gameMode;

    @Inject(method = "attackEntity", at = @At("HEAD"), cancellable = true)
    private void attackHook(final PlayerEntity player, final Entity target, final CallbackInfo ci) {
        if (target instanceof MultipartAwareEntity) {
            syncSelectedSlot();
            PlayerInteractMultipartEntity.attack(MinecraftClient.getInstance(), Hand.MAIN_HAND, (MultipartAwareEntity) target);
            if (gameMode != GameMode.SPECTATOR) {
                final MinecraftClient client = MinecraftClient.getInstance();
                final Vec3d pos = client.cameraEntity.getCameraPosVec(client.getRenderTickCounter().getTickDelta(true));
                final Vec3d dir = client.cameraEntity.getRotationVec(client.getRenderTickCounter().getTickDelta(true));
                final double reach = client.player.getEntityInteractionRange();
                ((MultipartAwareEntity) target).setNextDamagedPart(((MultipartAwareEntity) target).getBounds().raycast(pos, pos.add(dir.multiply(reach))));
                player.attack(target);
                player.resetLastAttackedTicks();
            }
            ci.cancel();
        }
    }

    @Inject(method = "interactEntity", at = @At("HEAD"), cancellable = true)
    private void interactHook(final PlayerEntity player, final Entity entity, final Hand hand, final CallbackInfoReturnable<ActionResult> cir) {
        if (entity instanceof MultipartAwareEntity) {
            syncSelectedSlot();
            cir.setReturnValue(PlayerInteractMultipartEntity.interact(MinecraftClient.getInstance(), hand, (MultipartAwareEntity) entity));
        }
    }
}
