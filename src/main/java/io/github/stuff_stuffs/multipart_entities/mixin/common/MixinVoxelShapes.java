package io.github.stuff_stuffs.multipart_entities.mixin.common;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VoxelShapes.class)
public class MixinVoxelShapes {
    @Inject(method = "cuboid(Lnet/minecraft/util/math/Box;)Lnet/minecraft/util/shape/VoxelShape;", at = @At("HEAD"), cancellable = true)
    private static void hook(final Box box, final CallbackInfoReturnable<VoxelShape> cir) {
        if (box instanceof CompoundOrientedBox) {
            cir.setReturnValue(((CompoundOrientedBox) box).toVoxelShape());
        }
    }
}
