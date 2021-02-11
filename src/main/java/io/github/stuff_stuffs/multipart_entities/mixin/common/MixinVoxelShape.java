package io.github.stuff_stuffs.multipart_entities.mixin.common;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShape.class)
public class MixinVoxelShape {
    @Unique
    private Box cachedBox;
    @Unique
    private Vec3d cachedVec;

    @Inject(method = "calculateMaxDistance(Lnet/minecraft/util/math/Direction$Axis;Lnet/minecraft/util/math/Box;D)D", at = @At("HEAD"), cancellable = true)
    private void hook(final Direction.Axis axis, final Box box, final double maxDist, final CallbackInfoReturnable<Double> cir) {
        if (box instanceof CompoundOrientedBox) {
            if (cachedBox != box) {
                cachedBox = box;
                cachedVec = ((CompoundOrientedBox) box).calculateMaxDistance((VoxelShape) (Object) this);
            }
            if(cachedVec==null) {
                cir.setReturnValue(maxDist);
                return;
            }
            if (maxDist < 0) {
                final double chosen = axis.choose(cachedVec.x, cachedVec.y, cachedVec.z);
                final double clamped = chosen <= 0 ? chosen : -Double.MAX_VALUE;
                cir.setReturnValue(Math.max(maxDist, clamped));
                return;
            } else {
                final double chosen = axis.choose(cachedVec.x, cachedVec.y, cachedVec.z);
                final double clamped = chosen >= 0 ? chosen : Double.MAX_VALUE;
                cir.setReturnValue(Math.min(maxDist, clamped));
                return;
            }
        }
    }
}
