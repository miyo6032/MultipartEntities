package io.github.stuff_stuffs.multipart_entities.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.world.World;

import java.util.Collections;

public class TestEntity extends AbstractMultipartAwareLivingEntity {
    private static final EntityBounds.Factory ENTITY_BOUNDS_FACTORY;

    public TestEntity(final EntityType<? extends LivingEntity> entityType, final World world) {
        super(entityType, world);
    }

    @Override
    protected EntityBounds createBounds() {
        return EntityBounds.builder().add("body").setBounds(1, 1, 1).setOffset(0, 0.5, 0).build().getFactory().create();
    }

    @Override
    public Iterable<ItemStack> getArmorItems() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getEquippedStack(final EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void equipStack(final EquipmentSlot slot, final ItemStack stack) {

    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        super.setPos(x, y, z);
        final EntityPart body = bounds.getPart("body");
        body.setX(getX());
        body.setY(getY());
        body.setZ(getZ());
    }

    @Override
    public Arm getMainArm() {
        return Arm.RIGHT;
    }

    static {
        ENTITY_BOUNDS_FACTORY = EntityBounds.builder().add("body").setBounds(1, 1, 1).setOffset(0, 0.5, 0).build().getFactory();
    }
}
