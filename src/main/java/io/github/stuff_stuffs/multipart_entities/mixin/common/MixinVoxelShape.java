package io.github.stuff_stuffs.multipart_entities.mixin.common;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShape.class)
public class MixinVoxelShape {
    @Inject(method = "calculateMaxDistance(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;D)D", at = @At("HEAD"), cancellable = true)
    private void hook(final Direction.Axis axis, final Box box, final double maxDist, final CallbackInfoReturnable<Double> cir) {
        if (box instanceof CompoundOrientedBox) {
            cir.setReturnValue(((CompoundOrientedBox) box).calculateMaxDistance(axis, (VoxelShape) (Object) this, maxDist));
        }
    }
}
