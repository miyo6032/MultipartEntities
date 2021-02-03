package io.github.stuff_stuffs.multipart_entities.mixin.common;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.util.shape.ArrayVoxelShape;
import net.minecraft.util.shape.VoxelSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArrayVoxelShape.class)
public interface InvokerArrayVoxelShape {
    @Invoker(value = "<init>")
    static ArrayVoxelShape init(final VoxelSet shape, final DoubleList xPoints, final DoubleList yPoints, final DoubleList zPoints) {
        throw new AssertionError();
    }
}
