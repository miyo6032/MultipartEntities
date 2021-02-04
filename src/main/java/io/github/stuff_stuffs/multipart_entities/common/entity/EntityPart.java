package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.OrientedBox;
import io.github.stuff_stuffs.multipart_entities.common.util.QuaternionD;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a hit box of an entity
 */
public final class EntityPart {
    private boolean changed = true;
    private double x, y, z;
    private final Box box;
    private double px, py, pz;
    private QuaternionD rotation;
    private @Nullable EntityPart parent;

    EntityPart(final @Nullable EntityPart parent, Box box, final boolean center) {
        this.parent = parent;
        rotation = QuaternionD.IDENTITY;
        if (center) {
            box = box.offset(-box.minX - box.getXLength() / 2, -box.minY - box.getXLength() / 2, -box.minZ - box.getXLength() / 2);
        }
        this.box = box;
    }

    void setParent(@Nullable final EntityPart parent) {
        this.parent = parent;
    }

    /**
     * @param x X coordinate relative to parent
     */
    public void setX(final double x) {
        this.x = x;
        changed = true;
    }

    /**
     * @param y Y coordinate relative to parent
     */
    public void setY(final double y) {
        this.y = y;
        changed = true;
    }

    /**
     * @param z Z coordinate relative to parent
     */
    public void setZ(final double z) {
        this.z = z;
        changed = true;
    }

    /**
     * @param px X coordinate of point this part should be rotated around
     */
    public void setPivotX(final double px) {
        this.px = px;
        changed = true;
    }

    /**
     * @param py X coordinate of point this part should be rotated around
     */
    public void setPivotY(final double py) {
        this.py = py;
        changed = true;
    }

    /**
     * @param pz X coordinate of point this part should be rotated around
     */
    public void setPivotZ(final double pz) {
        this.pz = pz;
        changed = true;
    }

    public void setRotation(final QuaternionD rotation) {
        this.rotation = rotation;
        changed = true;
    }

    public void rotate(final QuaternionD quaternion) {
        rotation = rotation.hamiltonProduct(quaternion);
        changed = true;
    }

    public void rotate(final double pitch, final double yaw, final double roll, final boolean degrees) {
        rotation = rotation.hamiltonProduct(new QuaternionD(pitch, yaw, roll, degrees));
        changed = true;
    }

    public void setRotation(final double pitch, final double yaw, final double roll, final boolean degrees) {
        rotation = new QuaternionD(pitch, yaw, roll, degrees);
        changed = true;
    }

    void setChanged(final boolean changed) {
        this.changed = changed;
    }

    boolean isChanged() {
        return changed;
    }

    /**
     * @return Oriented box represented by this EntityPart after all transformations have been applied
     */
    public OrientedBox getBox() {
        final OrientedBox orientedBox = new OrientedBox(box);
        return transformChild(orientedBox);
    }

    private OrientedBox transformChild(OrientedBox orientedBox) {
        if (parent != null) {
            orientedBox = parent.transformChild(orientedBox);
        }
        return orientedBox.transform(x, y, z, px, py, pz, rotation);
    }
}
