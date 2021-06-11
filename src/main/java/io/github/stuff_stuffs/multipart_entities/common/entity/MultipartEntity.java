package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import net.minecraft.util.math.Box;

public interface MultipartEntity {
    CompoundOrientedBox getCompoundBoundingBox(Box bounds);
}
