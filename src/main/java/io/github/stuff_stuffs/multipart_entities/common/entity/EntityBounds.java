package io.github.stuff_stuffs.multipart_entities.common.entity;

import io.github.stuff_stuffs.multipart_entities.common.util.CompoundOrientedBox;
import io.github.stuff_stuffs.multipart_entities.common.util.OrientedBox;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * A complete entity hit box hierarchy
 */
public final class EntityBounds {
    private CompoundOrientedBox cache;
    private final Map<String, EntityPart> partMap;

    EntityBounds(final Map<String, EntityPart> partMap) {
        this.partMap = partMap;
    }

    public boolean hasPart(final String name) {
        return partMap.get(name) != null;
    }

    /**
     * @param name Name of the part
     * @return The part with name, or null if it doesn't exist
     */
    public EntityPart getPart(final String name) {
        return partMap.get(name);
    }

    /**
     * @param start Starting position of raycast
     * @param end Ending position of raycast
     * @return Part name the ray first intersects, or null if no part was intersected
     */
    public @Nullable String raycast(final Vec3d start, final Vec3d end) {
        double t = 1.00001;
        String result = null;
        for (final Map.Entry<String, EntityPart> entry : partMap.entrySet()) {
            final double tmp = entry.getValue().getBox().raycast(start, end);
            if (tmp != -1 && tmp < t) {
                t = tmp;
                result = entry.getKey();
            }
        }
        return result;
    }

    /**
     * @param bounds Bounding box outside of which no collisions will be processed, should be the normal bounding box of the entity
     * @return Box compatible hierarchy of all the EntityParts
     */
    public CompoundOrientedBox getBox(final Box bounds) {
        boolean changed = cache == null;
        for (final EntityPart value : partMap.values()) {
            if (value.isChanged()) {
                changed = true;
                value.setChanged(false);
            }
        }
        if (changed) {
            final List<OrientedBox> parts = new ObjectArrayList<>(partMap.size());
            for (final EntityPart value : partMap.values()) {
                parts.add(value.getBox());
            }
            cache = new CompoundOrientedBox(bounds, parts);
        }
        return cache.withBounds(bounds);
    }

    /**
     * @return A new builder
     */
    public static EntityBoundsBuilder builder() {
        return new EntityBoundsBuilder();
    }

    public interface Factory {
        /**
         * @return A new entity bounds instance, should only be called once per entity at entity creation
         */
        EntityBounds create();
    }

    public static final class EntityBoundsBuilder {
        private final Map<String, EntityPartInfo> partInfos = new Object2ObjectLinkedOpenHashMap<>();

        EntityBoundsBuilder() {
        }

        EntityBoundsBuilder addInfo(final EntityPartInfo info) {
            if (info.parent != null && !partInfos.containsKey(info.parent)) {
                throw new RuntimeException("Unknown part: " + info.parent + ", did you register a child before a parent");
            }
            partInfos.put(info.name, info);
            return this;
        }

        /**
         * Adds a new hit box to the builder
         * @param name The name of the part, duplicates not allowed
         * @return The hit box builder
         */
        public EntityPartInfoBuilder add(final String name) {
            if (partInfos.containsKey(name)) {
                throw new RuntimeException("Duplicate part: " + name);
            }
            return new EntityPartInfoBuilder(this, name);
        }

        public Factory getFactory() {
            //defensive copy
            Map<String, EntityPartInfo> copy = new Object2ObjectLinkedOpenHashMap<>(partInfos);
            return () -> {
                final Map<String, EntityPart> partMap = new Object2ObjectOpenHashMap<>();
                for (final Map.Entry<String, EntityPartInfo> entry : copy.entrySet()) {
                    final EntityPartInfo info = entry.getValue();
                    final EntityPart entityPart = new EntityPart(info.parent != null ? partMap.get(info.parent) : null, info.bounds, false, info.name);
                    entityPart.setX(info.x);
                    entityPart.setY(info.y);
                    entityPart.setZ(info.z);
                    entityPart.setPivotX(info.px);
                    entityPart.setPivotY(info.py);
                    entityPart.setPivotZ(info.pz);
                    partMap.put(entry.getKey(), entityPart);
                }
                return new EntityBounds(partMap);
            };
        }
    }

    public static final class EntityPartInfoBuilder {
        final EntityBoundsBuilder builder;
        @Nullable String parent;
        final String name;
        double x, y, z;
        double px, py, pz;
        Box bounds;

        EntityPartInfoBuilder(final EntityBoundsBuilder builder, final String name) {
            this.builder = builder;
            this.name = name;
        }


        /**
         * Set position relative to parent, or absolute position if parent is null
         * @param x X pos
         * @param y Y Pos
         * @param z Z pos
         * @return this
         */
        public EntityPartInfoBuilder setPos(final double x, final double y, final double z) {
            this.x = x;
            this.y = y;
            this.z = z;
            return this;
        }

        /**
         * Set pivot position around which this will be rotated
         * @param x X pivot pos
         * @param y Y pivot pos
         * @param z Z pivot pos
         * @return this
         */
        public EntityPartInfoBuilder setPivot(final double x, final double y, final double z) {
            px = x;
            py = y;
            pz = z;
            return this;
        }

        /**
         * @param parent The name of the parent part, the parent part needs to be built first
         * @return this
         */
        public EntityPartInfoBuilder setParent(@Nullable final String parent) {
            this.parent = parent;
            return this;
        }

        /**
         * Set bounding box, you should probably use {@link #setBounds(double, double, double)} instead unless you know hat you are doing
         * @param bounds Bounding box
         * @return this
         */
        public EntityPartInfoBuilder setBounds(final Box bounds) {
            this.bounds = bounds;
            return this;
        }

        /**
         * Set dimensions of the box, you should prefer this to {@link #setBounds(Box)}
         * @param xLength width
         * @param yLength height
         * @param zLength depth
         * @return this
         */
        public EntityPartInfoBuilder setBounds(final double xLength, final double yLength, final double zLength) {
            bounds = new Box(-xLength / 2, -yLength / 2, -zLength / 2, xLength / 2, yLength / 2, zLength / 2);
            return this;
        }

        /**
         * @return Builds this box and returns the parent {@link EntityBounds.EntityBoundsBuilder}
         */
        public EntityBoundsBuilder build() {
            return builder.addInfo(new EntityPartInfo(parent, name, x, y, z, px, py, pz, bounds));
        }
    }

    private static final class EntityPartInfo {
        private final @Nullable String parent;
        private final String name;
        private final double x, y, z;
        private final double px, py, pz;
        private final Box bounds;

        private EntityPartInfo(final @Nullable String parent, final String name, final double x, final double y, final double z, final double px, final double py, final double pz, final Box bounds) {
            this.parent = parent;
            this.name = name;
            this.x = x;
            this.y = y;
            this.z = z;
            this.px = px;
            this.py = py;
            this.pz = pz;
            this.bounds = bounds;
        }
    }
}
