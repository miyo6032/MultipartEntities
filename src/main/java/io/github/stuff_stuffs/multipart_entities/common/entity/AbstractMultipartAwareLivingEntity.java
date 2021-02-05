package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * You probably want to extend this.
 * <p>
 * Also you must update override the setPos function and do the following:
 * <pre>
 * {@code
 * public class FooEntity extends AbstractMultipartAwareLivingEntity {
 *      private static final EntityBounds.Factory ENTITY_BOUNDS_FACTORY = ....;
 *
 *      protected EntityBounds createBounds() {
 *          return ENTITY_BOUNDS_FACTORY.create();
 *      }
 *
 *     @Override
 *     public void setPos(double x, double y, double z) {
 *         super.setPos(x, y, z);
 *         EntityPart body = bounds.getPart("body");
 *         body.setX(x);
 *         body.setY(y);
 *         body.setZ(z);
 *     }
 * }
 * }
 * </pre>
 * Where "body" is the central piece in the box hierarchy
 */
public abstract class AbstractMultipartAwareLivingEntity extends LivingEntity implements MultipartAwareEntity {
    protected EntityBounds bounds;
    protected @Nullable String nextDamagedPart;

    protected AbstractMultipartAwareLivingEntity(final EntityType<? extends LivingEntity> entityType, final World world) {
        super(entityType, world);
        bounds = createBounds();
    }

    protected abstract EntityBounds createBounds();

    @Override
    public CompoundOrientedBox getBoundingBox() {
        return bounds.getBox(super.getBoundingBox());
    }

    @Override
    public EntityBounds getBounds() {
        return bounds;
    }

    @Override
    public void setNextDamagedPart(@Nullable final String part) {
        nextDamagedPart = part;
    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        super.setPos(x, y, z);
        if (bounds == null) {
            bounds = createBounds();
        }
    }

    /**
     * @return This is needed for multipart entities
     */
    @Override
    public boolean isInsideWall() {
        if (noClip) {
            return false;
        } else {
            return world.getBlockCollisions(this, getBoundingBox().expand(-0.1), (blockState, blockPos) -> blockState.shouldSuffocate(world, blockPos)).findAny().isPresent();
        }
    }

    @Override
    public boolean damage(final DamageSource source, final float amount) {
        //Part dependent damage logic goes here
        final boolean r = super.damage(source, amount);
        nextDamagedPart = null;
        return r;
    }
}
