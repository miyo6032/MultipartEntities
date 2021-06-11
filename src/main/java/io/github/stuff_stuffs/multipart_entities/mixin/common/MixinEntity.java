package io.github.stuff_stuffs.multipart_entities.mixin.common;

import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartAwareEntity;
import io.github.stuff_stuffs.multipart_entities.common.entity.MultipartEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Inject(method = "getBoundingBox", at = @At("RETURN"), cancellable = true)
    private void getBoundingBox(CallbackInfoReturnable<Box> cir) {
        if (this instanceof MultipartEntity) {
            cir.setReturnValue(((MultipartEntity) this).getCompoundBoundingBox(cir.getReturnValue()));
        }
    }

    @Inject(method = "setPos", at = @At("TAIL"), cancellable = true)
    private void setPos(double x, double y, double z, CallbackInfo ci) {
        if (this instanceof MultipartAwareEntity) {
            ((MultipartAwareEntity)this).onSetPos(x, y, z);
        }
    }
}
