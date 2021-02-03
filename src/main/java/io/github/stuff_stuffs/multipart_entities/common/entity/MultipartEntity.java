package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;

/**
 * Implement on entities with multiple hit boxes.
 * To implement do the following:
 * <pre>
 * {@code
 * public class FooEntity extends Entity implements MultipartEntity {
 *     private final EntityBounds bounds = ENTITY_BOUNDS_FACTORY.get();
 *
 *     @Override
 *     public CompoundOrientedBox getBoundingBox() {
 *         return bounds.getBox(super.getBoundingBox());
 *     }
 * }
 * }
 * </pre>
 * Where ENTITY_BOUNDS_FACTORY is made using {@link EntityBounds#builder()}
 */
public interface MultipartEntity {
    CompoundOrientedBox getBoundingBox();
}
