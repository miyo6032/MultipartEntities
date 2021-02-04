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
 * Also you should update override the tick function and do the following
 * <pre>
 * {@code
 * public class FooEntity extends AbstractMultipartAwareLivingEntity {
 *     @Override
 *     public void tick() {
 *         super.tick();
 *         EntityPart body = bounds.getPart("body");
 *         body.setX(getX());
 *         body.setY(getY());
 *         body.setZ(getZ());
 *     }
 * }
 * }
 * </pre>
 * Where "body" is the central piece in the box hierarchy
 */
public abstract class AbstractMultipartAwareLivingEntity extends LivingEntity implements MultipartAwareEntity {
    protected final EntityBounds bounds;
    protected @Nullable String nextDamagedPart;

    protected AbstractMultipartAwareLivingEntity(final EntityType<? extends LivingEntity> entityType, final World world, final EntityBounds.Factory boundsFactory) {
        super(entityType, world);
        bounds = boundsFactory.create();
    }

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

    /**
     * @return This is needed for multipart entities
     */
    @Override
    public boolean isInsideWall() {
        if (noClip) {
            return false;
        } else {
            return world.getBlockCollisions(this, getBoundingBox(), (blockState, blockPos) -> blockState.shouldSuffocate(world, blockPos)).findAny().isPresent();
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
